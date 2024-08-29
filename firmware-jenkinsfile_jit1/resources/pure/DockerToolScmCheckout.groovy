package com.pure

class DockerToolScmCheckout implements Builder {
    def script

    DockerToolScmCheckout(script) {
        this.script = script
    }

    void checkoutDockerToolScm() {
        script.checkout scm: [
            $class: 'GitSCM',
            branches: [
                [
                    name: 'master'
                ]
            ],
            extensions: [
                [
                    $class: 'LocalBranch',
                    localBranch: 'master'
                ],
                [
                    $class: 'RelativeTargetDirectory',
                    relativeTargetDir: "${script.env.FW_BUILD_CONTAINERS_PATH}"
                ]
            ],
            userRemoteConfigs: [
                [
                    credentialsId: "${script.env.PURE_FIRMWARE_CRED}",
                    url: "${script.env.FW_BUILD_CONTAINERS_REPO}"
                ]
            ]
        ]
    }
}