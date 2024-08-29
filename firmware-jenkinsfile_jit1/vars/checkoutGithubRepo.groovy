import com.pure.GithubRepoCheckout

def call() {
    def checker = new GithubRepoCheckout(this)
    checker.checkoutGithubRepo()
}