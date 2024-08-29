package com.pure

class FullCleanupExecutor implements Builder {
    def script

    FullCleanupExecutor(script) {
        this.script = script
    }

    void executeFullCleanup() {
        script.loadLinuxScript(name: 'full_cleanup.sh')
        script.sh "./full_cleanup.sh"
    }
}