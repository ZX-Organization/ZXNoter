package team.zxorg.extensionloader.core

import com.google.gson.JsonObject
import team.zxorg.extensionloader.gson.GsonManager
import java.nio.file.Files
import java.nio.file.Path
 class Configuration(id: String) {
    private val configObject = HashMap<Class<*>, Any?>()
    private val jsonObject: JsonObject
    private val configPath: Path = Path.of("config", "$id.json")

    init {
        Files.createDirectories(configPath.parent)
        val json: String = if (Files.exists(configPath)) Files.readString(configPath) else "{}"
        jsonObject = GsonManager.parseJson(json).asJsonObject
    }

     /**
      * 获取配置
      * @param clazz 配置类
      * @return 配置对象
      */
    fun <T> get(clazz: Class<T>): T? {
        return configObject.getOrDefault(
            clazz, GsonManager.fromJson(jsonObject[clazz.toString()], clazz)
                ?: clazz.getDeclaredConstructor().newInstance()
        ).also {
            configObject.putIfAbsent(clazz, it)
        } as T
    }

     /**
      *  保存配置
      */
    fun save() {
        Files.createDirectories(configPath.parent)
        Files.write(configPath, GsonManager.toJson(configObject).toByteArray())
    }
}
