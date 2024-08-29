package com.pure

class PureAstyleChecker implements Builder {
    def script

    PureAstyleChecker(script) {
        this.script = script
    }

    void checkPureAstyle() {
        def is_pull_request = script.isPullRequest()

        // Check pure astyle
        if (is_pull_request) {
            // Run pure_astyle check before make.
            script.echo "List of changed files."
            for (commitFile in script.pullRequest.files) {
                script.echo "SHA: ${commitFile.sha} File Name: ${commitFile.filename} Status: ${commitFile.status}"

                // def comment = script.pullRequest.comment("SHA: ${commitFile.sha} File Name: ${commitFile.filename} Status: ${commitFile.status}")
                // script.echo "${comment}"
            }
        }
    }
}