package com.pure

class MakeCommandDeterminer implements Builder {
    def script

    MakeCommandDeterminer(script) {
        this.script = script
    }

    String determineMakeCommand() {
        // Determine the make command to run.
        // 2.7.x   "nice $FIRMWARE_TOOLS_PATH/bin/dmake -s release" -> 18.04
        // 2.8.x.  "nice $FIRMWARE_TOOLS_PATH/bin/dmake -s release" -> 18.04
        // 2.10.x  "nice make -s release FIRMWARE_TOOLS_PATH=$FIRMWARE_TOOLS_PATH/bin" -> 18.04
        // 2.11.x  "nice make -s release FIRMWARE_TOOLS_PATH=$FIRMWARE_TOOLS_PATH/bin" -> 20.04
        // 4.0.x   "nice make -s release FIRMWARE_TOOLS_PATH=$FIRMWARE_TOOLS_PATH/bin. -> 20.04
        // 4.1.x   "nice make -s release FIRMWARE_TOOLS_PATH=$FIRMWARE_TOOLS_PATH/bin" -> 20.04
        def release_branch = script.getReleaseBranch()
        def make_how = "make"

        script.sh "ls -lah ${script.FIRMWARE_TOOLS_PATH}bin/"
        if (release_branch.startsWith("release-2.7") || release_branch.startsWith("release-2.8")) {
            make_how = "${script.FIRMWARE_TOOLS_PATH}bin/dmake"
        }
        script.echo "make_how: ${make_how}"

        return make_how
    }
}