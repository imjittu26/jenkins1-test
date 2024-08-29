package com.pure

import spock.lang.Specification

class GithubRepoCheckoutSpec extends Specification {

    def "test checkoutGithubRepo for pull request"() {
        given:
        def script = Mock(GithubRepoCheckout) // Specify the type explicitly
        def pullRequest = Mock(PullRequest) // Specify the type explicitly
        script.getBaseRepoUrl() >> "https://github.com/PURE-Firmware/wssd-firmware"
        script.env >> [JOB_URL: "http://jenkins/job", BUILD_NUMBER: "123", JOB_BASE_NAME: "feature-branch", CHANGE_ID: "456", PURE_FIRMWARE_CRED: "ghe-app-cred"]
        script.STAGE_NAME >> "BuildFirmware"
        script.isPullRequest() >> true
        script.getRunTest() >> ["testName", false]
        script.getReleaseBranch() >> "release-branch"
        script.pullRequest >> pullRequest

        def checkout = new GithubRepoCheckout(script)

        when:
        checkout.checkoutGithubRepo()

        then:
        1 * script.echo("Checking out from feature-branch")
        1 * script.echo("Merging with release-branch")
        1 * script.checkout(_)
        1 * script.echo("GHE Checkout: pending -> Pending")
        1 * pullRequest.createStatus(status: 'pending', context: "fwjenkins2/stage/buildfirmware", description: "Pending", targetUrl: "http://jenkins/job/123")
    }

    def "test checkoutGithubRepo for non pull request"() {
        given:
        def script = Mock(GithubRepoCheckout) // Specify the type explicitly
        script.getBaseRepoUrl() >> "https://github.com/PURE-Firmware/wssd-firmware"
        script.env >> [JOB_URL: "http://jenkins/job", BUILD_NUMBER: "123", JOB_BASE_NAME: "feature-branch", CHANGE_ID: "456", PURE_FIRMWARE_CRED: "ghe-app-cred"]
        script.STAGE_NAME >> "BuildFirmware"
        script.isPullRequest() >> false
        script.getRunTest() >> ["testName", false]
        script.getReleaseBranch() >> "release-branch"

        def checkout = new GithubRepoCheckout(script)

        when:
        checkout.checkoutGithubRepo()

        then:
        1 * script.checkout(script.scm)
    }

    def "test checkoutGithubRepo with exception"() {
        given:
        def script = Mock(GithubRepoCheckout) // Specify the type explicitly
        def pullRequest = Mock(PullRequest) // Specify the type explicitly
        script.getBaseRepoUrl() >> "https://github.com/PURE-Firmware/wssd-firmware"
        script.env >> [JOB_URL: "http://jenkins/job", BUILD_NUMBER: "123", JOB_BASE_NAME: "feature-branch", CHANGE_ID: "456", PURE_FIRMWARE_CRED: "ghe-app-cred"]
        script.STAGE_NAME >> "BuildFirmware"
        script.isPullRequest() >> true
        script.getRunTest() >> ["testName", false]
        script.getReleaseBranch() >> "release-branch"
        script.pullRequest >> pullRequest

        def checkout = new GithubRepoCheckout(script)

        when:
        script.checkout(_) >> { throw new Exception("Checkout failed") }
        checkout.checkoutGithubRepo()

        then:
        1 * script.echo("Checking out from feature-branch")
        1 * script.echo("Merging with release-branch")
        1 * script.echo("GHE checkout failure: java.lang.Exception: Checkout failed")
        1 * script.echo("GHE Checkout: failure -> Failed to checkout from GitHub.")
        1 * pullRequest.createStatus(status: 'failure', context: "fwjenkins2/stage/buildfirmware", description: "Failed to checkout from GitHub.", targetUrl: "http://jenkins/job/123")
    }
}