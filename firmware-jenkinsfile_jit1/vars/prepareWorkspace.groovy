import com.pure.WorkspacePreparer

def call() {
    def preparer = new WorkspacePreparer(this)
    preparer.prepareWorkspace()
}