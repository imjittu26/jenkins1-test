package com.pure

class FirmwareBuilder implements Builder {
    def script

    FirmwareBuilder(script) {
        this.script = script
    }

    void buildFirmware() {
        def repo_url = script.getBaseRepoUrl()
        def ghe_url = "${script.env.JOB_URL}/${script.BUILD_NUMBER}"
        def ghe_status = 'pending'
        def ghe_context = "fwjenkins2/stage/${script.STAGE_NAME.toLowerCase()}"
        def ghe_description = "Pending"
        def make_how = script.determineMake()
        def is_pull_request = script.isPullRequest()
        def (runtest_name, is_runtest) = script.getRunTest()

        try {
            script.echo "Clean Firmware Build"
            script.sh "git clean -fxd"

            if (is_pull_request) {
                if ('quick_firmware_build' in script.pullRequest.labels.toList()) {
                    script.echo "Quick firmware build."
                    script.sh "${make_how} -s release UNIT_TEST=0 PY_CHECK=0"
                    ghe_status = 'success'
                    ghe_description = "Firmware Successfully built"
                } else if ('test_sbl_release' in script.pullRequest.labels.toList()) {
                    script.echo "Mock SBL firmware build."
                    script.sh "${make_how} -s BUILD_SBL_FROM_SOURCE=y"
                    ghe_status = 'success'
                } else {
                    script.echo "Default Pull Request Regular firmware build."
                    script.sh "${make_how} -s release WSSDSIM_BUILD=1"
                    ghe_status = 'success'
                    ghe_description = "Firmware Successfully built"
                }
            } else if (is_runtest) {
                script.echo "RUNTEST enabled. Make a quick build."
                script.sh "${make_how} -s release UNIT_TEST=0 PY_CHECK=0"
                ghe_status = 'success'
                ghe_description = "Firmware Successfully built"
            } else {
                script.echo "Regular firmware build."
                script.sh "${make_how} -s release WSSDSIM_BUILD=1"
                ghe_status = 'success'
                ghe_description = "Firmware Successfully built"
            }
        } catch (Exception e) {
            ghe_status = 'failure'
            ghe_description = "Failed to build firmware."
            script.error("Firmware build failed.")
            script.error("${e}")
        } finally {
            if (is_pull_request) {
                script.pullRequest.createStatus(status: ghe_status,
                    context: ghe_context,
                    description: ghe_description,
                    targetUrl: ghe_url)
            }
            script.archiveArtifacts(
                artifacts: 'build_firmware/results.xml,build_firmware/sbl*.bin,wssd*.zip,wssd-*.tgz,wssd-fw.md5,EFC/test/*.txt',
                followSymlinks: false,
                allowEmptyArchive: true,
                fingerprint: true
            )
        }

        if (script.fileExists("${script.env.WORKSPACE}/build_firmware/EFC/Board/sbl_image")) {
            script.echo "Copy G3 sbl file."
            script.fileOperations(
                [
                    script.fileCopyOperation(
                        excludes: '',
                        flattenFiles: false,
                        includes: 'EFC/Board/sbl_image/sbl*.bin',
                        targetLocation: "${script.env.WORKSPACE}"
                    )
                ]
            )
        }
    }
}