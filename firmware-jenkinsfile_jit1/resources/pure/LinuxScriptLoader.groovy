package com.pure

class LinuxScriptLoader implements Builder {
    def script

    LinuxScriptLoader(script) {
        this.script = script
    }

    void loadScript(Map config = [:]) {
        def scriptcontents = script.libraryResource "com/firmware/linux/${config.name}"
        script.writeFile file: "${config.name}", text: scriptcontents
        script.sh "chmod a+x ./${config.name}"
    }
}