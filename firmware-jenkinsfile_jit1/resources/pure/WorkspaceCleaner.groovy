package com.pure

class WorkspaceCleaner implements Builder {
    def script

    WorkspaceCleaner(script) {
        this.script = script
    }

    void cleanWorkspace() {
        def make_how = script.determineMake()
        script.loadLinuxScript(name: 'clean_workspace.sh')
        script.sh "./clean_workspace.sh ${make_how}"
    }
}