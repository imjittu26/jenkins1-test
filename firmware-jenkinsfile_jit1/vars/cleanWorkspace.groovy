import com.pure.WorkspaceCleaner

def call() {
    def cleaner = new WorkspaceCleaner(this)
    cleaner.cleanWorkspace()
}