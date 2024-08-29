import com.pure.PrecommitParametersDeterminer

def call() {
    def determiner = new PrecommitParametersDeterminer(this)
    return determiner.determinePrecommitParameters()
}