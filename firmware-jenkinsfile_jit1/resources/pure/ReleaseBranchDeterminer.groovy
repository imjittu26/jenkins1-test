package com.pure

class ReleaseBranchDeterminer implements Builder {
    def script

    ReleaseBranchDeterminer(script) {
        this.script = script
    }

    String determineReleaseBranch() {
        def release_branch = script.env.JOB_BASE_NAME
        if (release_branch.startsWith("PR-")) {
            release_branch = script.env.CHANGE_TARGET
        }
        return release_branch
    }
}