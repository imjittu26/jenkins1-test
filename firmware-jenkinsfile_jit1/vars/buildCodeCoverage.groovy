import com.pure.CodeCoverageBuilder

def call() {
     def builder = new CodeCoverageBuilder(this)
     builder.buildCodeCoverage()
}