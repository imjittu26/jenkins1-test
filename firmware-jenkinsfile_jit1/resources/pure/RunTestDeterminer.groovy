package com.pure

class RunTestDeterminer implements Builder {
    def script

    RunTestDeterminer(script) {
        this.script = script
    }

    List determineRunTest() {
        def runtest_name = ""
        def is_runtest = false
        // Get the last comment, parse it, and if it is a runtest, get the job name.
        // If not 0 meaning only if there are comments.
        if (script.isPullRequest()) {
            if (script.pullRequest.commentCount > 0) {
                def last_comment = script.pullRequest.comments.last()
                script.echo "last_comment: ${last_comment}"
                script.echo "body: ${last_comment.body}"
                if (last_comment.body.startsWith("RUNTEST")) {
                    is_runtest = true
                    def regex_runtest = last_comment.body =~ /^RUNTEST\:*\s*(.+)\s*$/
                    script.echo "Last comment body: ${last_comment.body}"
                    def regex_match = regex_runtest[0]
                    runtest_name = regex_match[1]
                    is_runtest = true
                }
            }
        }
        return [runtest_name, is_runtest]
    }
}