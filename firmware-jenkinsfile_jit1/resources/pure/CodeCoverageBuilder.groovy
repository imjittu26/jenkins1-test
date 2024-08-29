package com.pure

class CodeCoverageBuilder implements Builder {
    def script

    CodeCoverageBuilder(script) {
        this.script = script
    }

    void buildCodeCoverage() {
        def repo_url = script.getBaseRepoUrl()
        def ghe_url = "${script.env.JOB_URL}/${script.BUILD_NUMBER}"
        def ghe_status = 'pending'
        def ghe_context = "fwjenkins2/stage/${script.STAGE_NAME.toLowerCase()}"
        def ghe_description = "Pending"
        def make_how = script.determineMake()
        def is_pull_request = script.isPullRequest()
        def release_branch = script.getReleaseBranch()

        // Build our code coverage. G4 and G3 command option vary.
        try {
            if (release_branch.startsWith("release-2")) {
                script.sh "${make_how} -s code_coverage"
                ghe_status = 'success'
                ghe_description = "firmware Successfully built"
            } else {
                script.sh "${make_how} -s code_coverage_g4"
                ghe_status = 'success'
                ghe_description = "firmware Successfully built"
            }
        } catch (exc) {
            ghe_status = 'failure'
            ghe_description = "Docker Set Up - ${script.env}"
            script.error("Firmware build failed.")
            script.error("${script.env}")
        } finally {
            // Tell Github PR how we did.
            if (is_pull_request) {
                script.pullRequest.createStatus(
                    status: ghe_status,
                    context: ghe_context,
                    description: ghe_description,
                    targetUrl: ghe_url
                )
            }

            script.archiveArtifacts(
                artifacts: 'wssd-coverage*/**,wssd-coverage*.tgz',
                followSymlinks: false,
                allowEmptyArchive: true,
                fingerprint: true
            )
        }
    }
}