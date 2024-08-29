package com.pure

class DockerToolchainPreparer implements Builder {
    def script

    DockerToolchainPreparer(script) {
        this.script = script
    }

    void prepareDockerToolchain() {
        script.echo "------------ DOCKER TOOLCHAIN ------------"
        // Actual checkout SCM

        script.checkoutDockerToolScm()

        def branch_name = script.getReleaseBranch()
        def is_smi = branch_name.contains("smi-bringup")
        def tool_branch = is_smi ? "feature/g5sdktools" : "master"
        script.echo "Current branch = ${branch_name}, tool branch = ${tool_branch}"

        script.sh "cd ${script.env.FW_BUILD_CONTAINERS_PATH}/docker; FW_TOOLS_BRANCH=${tool_branch} ./deploy.sh local"
        script.echo "------------ END DOCKER TOOLCHAIN ------------"

        // Make sure we are running all the dockers.
        def D14 = script.sh(script: "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_14.04 echo '14.04 working'", returnStdout: true)
        def D18 = script.sh(script: "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_18.04 echo '18.04 working'", returnStdout: true)
        def D20 = script.sh(script: "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_20.04 echo '20.04 working'", returnStdout: true)
        def D22 = script.sh(script: "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_22.04 echo '22.04 working'", returnStdout: true)
        def D24 = script.sh(script: "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_24.04 echo '24.04 working'", returnStdout: true)
        def CD9 = script.sh(script: "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_centos9 echo 'centos9 working'", returnStdout: true)

        if (D14 && D18 && D20 && D22 && D24 && CD9) {
            script.echo "${script.env.D_GOOD}"
        } else {
            script.echo "${script.env.D_BAD}"
            return 1
        }
    }
}