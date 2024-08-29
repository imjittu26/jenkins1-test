package com.pure

class PrecommitParametersDeterminer implements Builder {
    def script

    PrecommitParametersDeterminer(script) {
        this.script = script
    }

    List determinePrecommitParameters() {
        // Default precommit job name.
        def precommit_job = "docker-code-review-wssd.targeted_test-wssd.code-precommit"
        def distro_version = "20.04"
        def testbed_label = "wssd_precommit"
        def test_name = "wssd.precommit_g4_test"
        def release_branch = script.getReleaseBranch()

        // Legacy way to run precommit. Please restore if required.
        if (release_branch.startsWith("release-2.7")) {
            // precommit_job = "docker-code-review-wssd.targeted_test-wssd.precommit-1804_2_7_x"
            distro_version = "18.04"
            testbed_label = "wssd_2_7_x"
            test_name = "wssd.precommit_test"
        } else if (release_branch.startsWith("release-2.8")) {
            // precommit_job = "docker-code-review-wssd.targeted_test-wssd.precommit-1804_2_8_x"
            distro_version = "18.04"
            testbed_label = "wssd_2_8_x"
            test_name = "wssd.precommit_test"
        } else if (release_branch.startsWith("release-2.10")) {
            // precommit_job = "docker-code-review-wssd.targeted_test-wssd.precommit-1804_2_10_x"
            distro_version = "20.04"
            testbed_label = "wssd_2_10_x"
            test_name = "wssd.precommit_test"
        } else if (release_branch.startsWith("release-2.11")) {
            // precommit_job = "docker-code-review-wssd.targeted_test-wssd.precommit-1804_2_11_x"
            distro_version = "20.04"
            testbed_label = "wssd_2_11_x"
            test_name = "wssd.precommit_test"
        } else if (release_branch.startsWith("release-4.0")) {
            // precommit_job = "docker-code-review-wssd.targeted_test-wssd.precommit-1804_4_0_x"
            distro_version = "20.04"
            testbed_label = "wssd_precommit"
        } else if (release_branch.startsWith("release-4.1")) {
            // precommit_job = "docker-code-review-wssd.targeted_test-wssd.precommit-1804_4_1_x"
            distro_version = "20.04"
            testbed_label = "wssd_code_review_4_1"
        } else if (release_branch.startsWith("release-4.2")) {
            // precommit_job = "docker-code-review-wssd.targeted_test-wssd.precommit-1804_4_2_x"
            distro_version = "20.04"
            testbed_label = "wssd_code_review_4_2"
        } else {
            // precommit_job = "docker-code-review-wssd.targeted_test-wssd.code-precommit-merge_pool"
            distro_version = "20.04"
            testbed_label = "wssd_precommit"
        }
        return [precommit_job, distro_version, testbed_label, test_name]
    }
}