import com.pure.TestReleaseBuilder

def call() {
    def builder = new TestReleaseBuilder(this)
    builder.buildTestRelease()
}