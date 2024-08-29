import com.pure.BaseRepoUrlDeterminer

def call() {
    def getter = new BaseRepoUrlDeterminer(this)
    return getter.determineBaseRepoUrl()
}