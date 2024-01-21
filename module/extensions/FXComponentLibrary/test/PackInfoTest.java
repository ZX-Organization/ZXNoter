import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PackInfoTest {
    public static void main(String[] args) throws IOException {
        Path packPath = Path.of("module/extensions/FXComponentLibrary/resources/assets/fxComponentLibrary/baseResourcePack/icon/line");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject packInfo = new JsonObject();
        JsonArray resources = new JsonArray();
        Files.walk(packPath).forEach(file -> {
            if (Files.isDirectory(file)) {

            } else {
                resources.add(packPath.relativize(file).toString().replaceAll("\\\\", "/"));
            }
        });

        packInfo.addProperty("name", "Icon Pack");
        packInfo.add("resources", resources);

        Files.write(packPath.resolve("pack.json5"), gson.toJson(packInfo).getBytes(StandardCharsets.UTF_8));
    }
}
