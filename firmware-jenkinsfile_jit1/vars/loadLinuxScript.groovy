import com.pure.LinuxScriptLoader

def call(Map config = [:]) {
    def loader = new LinuxScriptLoader(this)
    loader.loadScript(config)
}