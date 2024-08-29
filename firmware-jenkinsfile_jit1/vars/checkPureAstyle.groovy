import com.pure.PureAstyleChecker

def call() {
    def checker = new PureAstyleChecker(this)
    checker.checkPureAstyle()
}