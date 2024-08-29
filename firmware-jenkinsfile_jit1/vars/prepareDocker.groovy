import com.pure.DockerToolchainPreparer

def call() {
    def preparer = new DockerToolchainPreparer(this)
    preparer.prepareDockerToolchain()
}