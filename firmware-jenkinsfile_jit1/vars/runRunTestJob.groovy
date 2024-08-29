import com.pure.RunTestJobExecutor

def call() {
    def runner = new RunTestJobExecutor(this)
    runner.executeRunTestJob()
}