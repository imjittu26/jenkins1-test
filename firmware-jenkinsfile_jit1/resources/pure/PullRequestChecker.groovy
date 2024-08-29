package com.pure

class PullRequestChecker implements Builder {
    def script

    PullRequestChecker(script) {
        this.script = script
    }

    boolean isPullRequest() {
        def is_pull_request = false
        def release_branch = script.env.JOB_BASE_NAME
        if (release_branch.startsWith("PR-")) {
            is_pull_request = true
        }
        return is_pull_request
    }
}