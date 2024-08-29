import com.pure.PrecommitTestRunner

def call(Map config) {
    def runner = new PrecommitTestRunner(this)
    runner.runPrecommitTest(config)
}