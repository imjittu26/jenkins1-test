import com.pure.FirmwareBuilder

def call() {
     def builder = new FirmwareBuilder(this)
     builder.buildFirmware()
}