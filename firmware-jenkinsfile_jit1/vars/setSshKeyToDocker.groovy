import com.pure.DockerSshConfigurator

def call() {
    def setter = new DockerSshConfigurator(this)
    setter.configureDockerSsh()
}