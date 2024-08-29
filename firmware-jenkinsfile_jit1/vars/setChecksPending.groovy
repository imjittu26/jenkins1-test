import com.pure.PendingStatusSetter

def call() {
    def setter = new PendingStatusSetter(this)
    setter.setPendingStatus()
}