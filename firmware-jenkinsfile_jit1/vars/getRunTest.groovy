import com.pure.RunTestDeterminer

def call() {
    def getter = new RunTestDeterminer(this)
    return getter.determineRunTest()
}