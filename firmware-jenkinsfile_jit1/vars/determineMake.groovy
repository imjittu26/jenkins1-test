import com.pure.MakeCommandDeterminer

def call() {
    def determiner = new MakeCommandDeterminer(this)
    return determiner.determineMakeCommand()
}