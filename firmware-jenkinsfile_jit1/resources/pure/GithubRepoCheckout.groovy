package com.pure

class GithubRepoCheckout implements Builder {
    def script

    GithubRepoCheckout(script) {
        this.script = script
    }

    void checkoutGithubRepo() {
        def baseRepoUrlDeterminer = new BaseRepoUrlDeterminer(script)
        def repo_url = baseRepoUrlDeterminer.determineBaseRepoUrl()
        def ghe_url = "${script.env.JOB_URL}/${script.BUILD_NUMBER}"
        def ghe_status = 'pending'
        def ghe_context = "fwjenkins2/stage/${script.STAGE_NAME.toLowerCase()}"
        def ghe_description = "Pending"
        def is_pull_request = script.isPullRequest()
        def (runtest_name, is_runtest) = script.getRunTest()
        def release_branch = script.getReleaseBranch()

        // Get the proper repo.
        if (is_pull_request) {
            script.echo "Checking out from ${script.env.JOB_BASE_NAME}"
            script.echo "Merging with ${release_branch}"
            try {
                script.checkout([
                    $class: 'GitSCM',
                    branches: [[name: release_branch]], // FETCH_HEAD
                    extensions: [
                        [$class: 'CloneOption', reference: '/home/jenkins/reference/PURE-Firmware/wssd-firmware'],
                        [$class: 'PreBuildMerge',
                            options: [
                                mergeRemote: "origin",
                                mergeTarget: "${script.env.JOB_BASE_NAME}"
                            ]
                        ]
                    ],
                    userRemoteConfigs: [[
                        refspec: "+refs/pull/${script.env.CHANGE_ID}/head:refs/remotes/origin/PR-${script.env.CHANGE_ID}",
                        credentialsId: "${script.env.PURE_FIRMWARE_CRED}",
                        url: "${repo_url}"
                    ]]
                ])
            } catch (exc) {
                ghe_status = "failure"
                ghe_description = "Failed to checkout from GitHub."
                script.echo "GHE checkout failure: ${exc}"
            } finally {
                script.echo "GHE Checkout: ${ghe_status} -> ${ghe_description}"
                script.pullRequest.createStatus(
                    status: ghe_status,
                    context: ghe_context,
                    description: ghe_description,
                    targetUrl: ghe_url
                )
            }
        } else {
            script.checkout script.scm
        }
    }
        def getScm() {
        // Mock implementation
        return script.scm
    }
}