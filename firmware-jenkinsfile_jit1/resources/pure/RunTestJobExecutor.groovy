package com.pure

class RunTestJobExecutor implements Builder {
    def script

    RunTestJobExecutor(script) {
        this.script = script
    }

    void executeRunTestJob() {
        // Are the artifacts being copied here?
        def (run_test_name, is_runtest) = script.getRunTest()
        def ghe_url = "${script.env.JOB_URL}/build/${script.BUILD_NUMBER}"
        def ghe_status = 'pending'
        def ghe_context = "fwjenkins2/stage/${script.STAGE_NAME.toLowerCase()}"
        def ghe_description = "Pending"

        script.echo "Running test: ${run_test_name}"
        def job_exec_details = script.build job: script.env.custom_job_name_master,
            wait: true,
            propagate: false,
            parameters: [
                script.string(name: 'CUSTOM_JOB_NAME', value: run_test_name),
                script.string(name: 'FIRMWARE_JOB_NAME', value: script.env.JOB_NAME),
                script.string(name: 'FIRMWARE_BUILD_NUMBER', value: script.env.BUILD_NUMBER)
            ]
        ghe_url = "${script.JENKINS_URL}job/${script.env.custom_job_name_master}/${job_exec_details.getNumber()}"

        // Set status
        if (job_exec_details.getResult() == "SUCCESS") {
            ghe_status = 'success'
            ghe_description = "RUNTEST Passed - build: ${job_exec_details.getDescription()}"
        } else {
            ghe_status = 'failure'
            ghe_description = "RUNTEST Failed - build: ${job_exec_details.getDescription()}"
        }

        // Announce result:
        script.pullRequest.createStatus(
            status: ghe_status,
            context: ghe_context,
            description: ghe_description,
            targetUrl: ghe_url
        )

        // Set status and we must use a condition because declarative pipelines are awful.
        if ("FAILURE" == job_exec_details.getResult()) {
            script.error("${job_exec_details.getResult()}; JOB: ${run_test_name} BUILD NUMBER: ${job_exec_details.getNumber()}")
        } else if ("ABORTED" == job_exec_details.getResult()) {
            script.unstable(message: "${job_exec_details.getResult()}; JOB: ${run_test_name} BUILD NUMBER: ${job_exec_details.getNumber()}")
        }
        // Default will be successful.

        script.echo "job_exec_details.getNumber(): ${job_exec_details.getNumber()}"
        script.echo "job_exec_details.getResult(): ${job_exec_details.getResult()}"
        script.echo "job_exec_details.getDescription(): ${job_exec_details.getDescription()}"
        script.echo "job_exec_details.getId(): ${job_exec_details.getId()}"
        script.echo "job_exec_details.getDisplayName(): ${job_exec_details.getDisplayName()}"
        script.echo "job_exec_details.getStartTimeInMillis(): ${job_exec_details.getStartTimeInMillis()}"
    }
}