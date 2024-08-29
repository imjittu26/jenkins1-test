import com.pure.JobUrlFormatter

def call() {
    def getter = new JobUrlFormatter(this)
    return getter.formatJobUrl()
}