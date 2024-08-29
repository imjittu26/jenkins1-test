import com.pure.DockerToolScmCheckout

def call() {
    def checker = new DockerToolScmCheckout(this)
    checker.checkoutDockerToolScm()
}