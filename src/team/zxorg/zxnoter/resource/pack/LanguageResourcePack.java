package team.zxorg.zxnoter.resource.pack;

import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.ResourceType;

import java.nio.file.Path;
import java.util.HashMap;

public class LanguageResourcePack extends BaseResourcePack {
    private HashMap<String, String> languages;


    public LanguageResourcePack(ResourcePack pack, ResourceType type, Path jsonPath) {
        super(pack, type, jsonPath);
    }


    @Override
    public String toString() {
        return "语言资源[" +
                "id:'" + getResourceId() + '\'' +
                ", 语言数:" + languages.size() +
                ']';
    }

    @Override
    public String getName() {
        return getLanguageContent("language-name");
    }

    /**
     * 获取语言内容
     *
     * @param key 语言key
     * @return 内容
     */
    public String getLanguageContent(String key) {
        String value = languages.get(key);
        if (value != null) {
            return value;
        }
        return getLanguageContent("language.loss");//如果不存在则返回丢失内容
    }

    /**
     * 获取语言全部资源
     *
     * @return 语言map
     */
    public HashMap<String, String> getLanguages() {
        return languages;
    }


    @Override
    public void reload() {
        languages = getInfo().getJSONObject("languages").toJavaObject(HashMap.class);
    }
}
