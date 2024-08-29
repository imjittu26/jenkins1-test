import com.pure.FullCleanupExecutor

def call() {
    def cleaner = new FullCleanupExecutor(this)
    cleaner.executeFullCleanup()
}