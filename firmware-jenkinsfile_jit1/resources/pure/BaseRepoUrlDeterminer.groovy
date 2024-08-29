package com.pure

class BaseRepoUrlDeterminer implements Builder {
    def script

    BaseRepoUrlDeterminer(script) {
        this.script = script
    }

    String determineBaseRepoUrl() {
        // Determine the base repo for PRs and merge builds.
        // BASE_REPO_URL='https://github.com/PURE-Firmware/wssd-firmware'
        // CHANGE_URL='https://github.com/PURE-Firmware/firmware-jenkins-automation/pull/15'
        def repo_url = ""
        if (script.env.JOB_BASE_NAME.startsWith("PR-")) {
            def regex_base = script.env.CHANGE_URL =~ /^(https.+.)pull.*$/
            script.echo "regex_base ${regex_base}"
            def regex_match = regex_base[0]
            script.echo "regex match: ${regex_match}"
            repo_url = regex_match[1]
            script.echo "regex_base ${regex_base}"
        } else {
            repo_url = "${script.env.BASE_REPO_URL}"
        }
        script.echo "repo_url ${repo_url}"

        return repo_url
    }
}