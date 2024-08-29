import com.pure.ReleaseBranchDeterminer

def call() {
    def getter = new ReleaseBranchDeterminer(this)
    return getter.determineReleaseBranch()
}