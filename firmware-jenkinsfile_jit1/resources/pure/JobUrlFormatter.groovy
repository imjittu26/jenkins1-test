package com.pure

class JobUrlFormatter implements Builder {
    def script

    JobUrlFormatter(script) {
        this.script = script
    }

    String formatJobUrl() {
        def regex = ~":8080"
        String job_url = script.env.JOB_URL.replaceAll(regex, "")
        return job_url
    }
}