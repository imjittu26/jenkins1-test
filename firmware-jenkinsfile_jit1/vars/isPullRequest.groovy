import com.pure.PullRequestChecker

def call() {
    def checker = new PullRequestChecker(this)
    return checker.isPullRequest()
}