package com.pure

class PrecommitTestRunner implements Builder {
    def script

    PrecommitTestRunner(script) {
        this.script = script
    }

    void runPrecommitTest(Map config = [:]) {
        def ghe_url = "${script.env.JOB_URL}/build/${script.BUILD_NUMBER}"
        def ghe_status = 'pending'
        def ghe_context = "fwjenkins2/stage/${script.env.STAGE_NAME.toLowerCase()}"
        def ghe_description = "Pending"

        // Execute precommit.
        def job_exec_details = script.build job: config.precommit_job,
            wait: true,
            propagate: false,
            parameters: [
                [$class: 'StringParameterValue', name: 'FIRMWARE_JOB_NAME', value: script.env.JOB_NAME],
                [$class: 'StringParameterValue', name: 'FIRMWARE_BUILD_NUMBER', value: script.env.BUILD_NUMBER],
                [$class: 'StringParameterValue', name: 'TEST_LABEL', value: config.testbed_label],
                [$class: 'StringParameterValue', name: 'DISTRO_VERSION', value: config.distro_version],
                [$class: 'StringParameterValue', name: 'ARTIFACTS_TO_COPY', value: script.env.TEST_ARTIFACTS_TO_COPY],
                [$class: 'StringParameterValue', name: 'ARTIFACTS_NOT_TO_COPY', value: script.env.RELEASE_TEST_ARTIFACTS_TO_COPY],
                [$class: 'StringParameterValue', name: 'JOB_NAME', value: config.test_name]
            ]

        ghe_url = "${script.JENKINS_URL}job/${config.precommit_job}/${job_exec_details.getNumber()}"
        if (job_exec_details.getResult() == "SUCCESS") {
            ghe_status = 'success'
            ghe_description = "PRECOMMIT Passed - build: ${job_exec_details.getDescription()}"
        } else {
            ghe_status = 'failure'
            ghe_description = "PRECOMMIT Failed - build: ${job_exec_details.getDescription()}"
        }

        script.echo "job_exec_details.getNumber(): ${job_exec_details.getNumber()}"
        script.echo "job_exec_details.getResult(): ${job_exec_details.getResult()}"
        script.echo "job_exec_details.getDescription(): ${job_exec_details.getDescription()}"
        script.echo "job_exec_details.getId(): ${job_exec_details.getId()}"
        script.echo "job_exec_details.getDisplayName(): ${job_exec_details.getDisplayName()}"
        script.echo "job_exec_details.getStartTimeInMillis(): ${job_exec_details.getStartTimeInMillis()}"

        // Announce result:
        script.pullRequest.createStatus(
            status: ghe_status,
            context: ghe_context,
            description: ghe_description,
            targetUrl: ghe_url
        )

        // Set status and we must use a condition because declarative pipelines are awful.
        if ("FAILURE" == job_exec_details.getResult()) {
            script.error("${job_exec_details.getResult()}; JOB: ${config.precommit_job} BUILD NUMBER: ${job_exec_details.getNumber()}")
        } else if ("ABORTED" == job_exec_details.getResult()) {
            script.unstable(message: "${job_exec_details.getResult()}; JOB: ${config.precommit_job} BUILD NUMBER: ${job_exec_details.getNumber()}")
        }
        // Default will be successful.
    }
}