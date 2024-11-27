import java.io.File

object Config {
    val runtimeDir = File("${System.getProperty("user.dir")}/runtime")
    val extensionsDir = File(runtimeDir, "extensions")
    val resourcepacksDir = File(runtimeDir, "resourcepacks")
    val logsDir = File(runtimeDir, "logs")
    val configDir = File(runtimeDir, "config")
}
