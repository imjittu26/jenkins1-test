import com.pure.BuildDescriptionChanger

def call() {
    def changer = new BuildDescriptionChanger(this)
    changer.changeBuildDescription()
}