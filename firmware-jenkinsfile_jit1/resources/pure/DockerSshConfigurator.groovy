package com.pure

class DockerSshConfigurator implements Builder {
    def script

    DockerSshConfigurator(script) {
        this.script = script
    }

    void configureDockerSsh() {
        def is_pull_request = script.isPullRequest()
        def release_branch = script.getReleaseBranch()
        try {
            // Copy root key to the appropriate Docker depending on the release.
            if (release_branch.startsWith("release-2.10") || release_branch.startsWith("release-2.7") || release_branch.startsWith("release-2.8")) {
                script.sh "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_18.04 mkdir -p /root/.ssh"
                script.sh "cp /root/.ssh/config docker_ssh_config"
                script.sh "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_18.04 cp docker_ssh_config /root/.ssh/config"
                script.sh "export UBUNTU_VER='18.04'"
            } else if (release_branch.startsWith("release-2.11") || release_branch.startsWith("release-4.0") || release_branch.startsWith("release-4.1")) {
                script.sh "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_20.04 mkdir -p /root/.ssh"
                script.sh "cp /root/.ssh/config docker_ssh_config"
                script.sh "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_20.04 cp docker_ssh_config /root/.ssh/config"
                script.sh "export UBUNTU_VER='20.04'"
            } else {
                // release-4.x.x and the rest will default here.
                script.sh "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_20.04 mkdir -p /root/.ssh"
                script.sh "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_22.04 mkdir -p /root/.ssh"
                script.sh "cp /root/.ssh/config docker_ssh_config"
                script.sh "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_20.04 cp docker_ssh_config /root/.ssh/config"
                script.sh "${script.env.FIRMWARE_TOOLS_PATH}/bin/run_22.04 cp docker_ssh_config /root/.ssh/config"
                script.sh "export UBUNTU_VER='22.04'"
            }
        } catch (Exception e) {
            script.error("Docker copying ssh to docker failed.")
            script.error("${e}")
            if (is_pull_request) {
                script.pullRequest.createStatus(
                    status: 'error',
                    context: "fwjenkins2/stage/${script.env.STAGE_NAME.toLowerCase()}",
                    description: "Docker Set Up failed.",
                    targetUrl: "${script.env.JOB_URL}/${script.env.BUILD_NUMBER}"
                )
            }
        }
    }
}