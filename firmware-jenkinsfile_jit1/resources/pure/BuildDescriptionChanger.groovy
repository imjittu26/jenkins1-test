package com.pure

class BuildDescriptionChanger implements Builder {
    def script

    BuildDescriptionChanger(script) {
        this.script = script
    }

    void changeBuildDescription() {
        def repo_url = script.getBaseRepoUrl()
        def ghe_url = "${script.env.JOB_URL}/${script.BUILD_NUMBER}"
        def ghe_status = 'pending'
        def ghe_context = "fwjenkins2/stage/${script.STAGE_NAME.toLowerCase()}"
        def ghe_description = "Pending"
        def is_pull_request = script.isPullRequest()

        // Change the build description for non-PR builds.
        if (!is_pull_request) {
            script.echo "Change build description."
            def VERSION_NAME = "undef"
            def VERSION_HASH = "undef"
            try {
                if (script.env.JOB_BASE_NAME.startsWith("release-2")) {
                    VERSION_NAME = script.sh(script: './utils/version.py name', returnStdout: true).trim()
                    VERSION_HASH = script.sh(script: './utils/version.py hash', returnStdout: true).trim()
                    script.currentBuild.description = "build_type:${VERSION_NAME} hash:${VERSION_HASH}"
                } else {
                    def VERSION_JSON = script.sh(script: './utils/version.py --allow-test json', returnStdout: true).trim()
                    def version_props = script.readJSON(text: VERSION_JSON)
                    script.currentBuild.description = "build_type:${version_props['name']} hash:${version_props['hash']}"
                }
            } catch (exc) {
                ghe_status = 'error'
                ghe_description = "Could not determine the version hash or name."
                script.echo "Could not determine the version hash or name."
                script.echo "${exc}"
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
            }
        }
    }
}