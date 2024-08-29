package com.pure

class TestReleaseBuilder implements Builder {
    def script

    TestReleaseBuilder(script) {
        this.script = script
    }

    void buildTestRelease() {
        def make_how = script.determineMake()
        // Master only run release on parallel branch.
        try {
            script.sh "${make_how} -s release UNIT_TEST=1 PY_CHECK=1 FW_TEST_9999=release"
        } catch (env) {
            script.error("making test release failed.")
            script.error("${env}")
        } finally {
            // Copy files
            def all_files = script.findFiles(glob: "*")
            script.echo("All files: ${all_files}")
            def release_files = script.findFiles(glob: "${script.env.RELEASE_TEST_ARTIFACTS_TO_COPY}")
            script.echo "Release files: ${release_files}"
            if (!release_files) {
                script.error("${script.env.RELEASE_TEST_ARTIFACTS_TO_COPY} files do not exist. Cannot proceed.")
                return 1
            }
            script.archiveArtifacts(
                artifacts: "${script.env.RELEASE_TEST_ARTIFACTS_TO_COPY}",
                followSymlinks: false,
                allowEmptyArchive: true,
                fingerprint: true
            )
        }
    }
}