package team.zxorg.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.net.URL;
import java.util.HashMap;

public class LanguageInfo {
    private final URL from;
    private final String code;
    private final HashMap<String, String> languages;

    public String getCode() {
        return code;
    }

    public LanguageInfo(URL from, JsonElement languagesJson) {
        this.from = from;
        this.languages = new HashMap<>();
        traverseLanguageElement(languagesJson, null);
        code = languages.get("language.code");
        if (code == null)
            ZXLogger.warning("Language code is null " + from);
    }

    public URL getFrom() {
        return from;
    }

    public HashMap<String, String> getLanguages() {
        return languages;
    }

    private void traverseLanguageElement(JsonElement jsonElement, String currentPath) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (String key : jsonObject.keySet()) {
                String newPath = (currentPath == null ? "" : currentPath + ".") + key;
                JsonElement childElement = jsonObject.get(key);
                traverseLanguageElement(childElement, newPath);
            }
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            languages.put(currentPath, jsonPrimitive.getAsString());
        }
    }
}
