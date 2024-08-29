package com.pure

class WorkspacePreparer implements Builder {
    def script

    WorkspacePreparer(script) {
        this.script = script
    }

    void prepareWorkspace() {
        script.loadLinuxScript(name: 'prepare_workspace.sh')
        script.sh "./prepare_workspace.sh"
    }
}