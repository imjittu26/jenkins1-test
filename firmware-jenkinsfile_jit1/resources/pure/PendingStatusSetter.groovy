package com.pure

class PendingStatusSetter implements Builder {
    def script

    PendingStatusSetter(script) {
        this.script = script
    }

    void setPendingStatus() {
        def is_pull_request = script.isPullRequest()

        // Set status
        if (is_pull_request) {
            // Create a pending status if we made it here:
            script.pullRequest.createStatus(
                status: 'pending',
                context: "fwjenkins2/stage/${script.env.STAGE_NAME.toLowerCase()}",
                description: "Pending",
                targetUrl: "${script.env.JOB_URL}/${script.env.BUILD_NUMBER}"
            )
        }
    }
}