package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.resource.type.LanguageResource;
import team.zxorg.zxnoter.resource.type.Resource;
import team.zxorg.zxnoter.resource.type.ResourceType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ResourcePack {
    /**
     * 唯一表示id (禁止使用'.' 用于分隔域)
     */
    private final String id;
    /**
     * 资源包作者
     */
    private final String author;
    /**
     * 资源包路径
     */
    private final Path packPath;
    /**
     * 资源包信息
     */
    private final JSONObject packInfo;
    /**
     * 所有的资源
     */
    private final HashMap<ResourceType, HashMap<String, Resource>> allResources = new HashMap<>();


    public Resource getResources(ResourceType type, String subId) {
        return allResources.get(type).get(subId);
    }

    @Override
    public String toString() {
        return "资源包[" +
                "名称:'" + getName() + '\'' +
                ", id:'" + id + '\'' +
                ", 路径:'" + packPath + '\'' +
                ']';
    }


    public ResourcePack(Path resourcePackPath) {
        //初始化所有资源map
        for (ResourceType type : ResourceType.values()) {
            allResources.put(type, new HashMap<>());
        }

        packPath = resourcePackPath;
        try {
            packInfo = JSON.parseObject(Files.newInputStream(resourcePackPath.resolve("resource-pack.json")));
            id = packInfo.getString("id");
            if (id == null) {
                throw new RuntimeException("ResourcePack:没有id属性");
            }
            String authorsTemp = packInfo.getString("author");
            author = (authorsTemp != null) ? authorsTemp : "";
        } catch (IOException e) {
            throw new RuntimeException("ResourcePack:资源包读取异常");
        }

        reloadResources();
    }

    public String getName() {
        return getLanguageContent("resource-pack." + id + ".name");
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescribe() {
        return getLanguageContent("resource-pack." + id + ".describe");
    }

    public Path getPackPath() {
        return packPath;
    }


    /**
     * 重载资源
     */
    public void reloadResources() {
        for (ResourceType type : ResourceType.values()) {
            //载入对应资源
            JSONArray resList = packInfo.getJSONObject("resources").getJSONArray(type.name());
            Resource resource;
            for (int i = 0; i < resList.size(); i++) {
                resource = type.build(this, packPath.resolve(resList.getString(i)));
                resource.reload();
                allResources.get(type).put(resource.getResourceId(), resource);
            }
        }
    }


    /**
     * 获取本地化语言内容 (尝试使用本地语言 如果没有则使用默认语言)
     *
     * @param key 语言key
     * @return 本地化语言内容
     */
    public String getLanguageContent(String key) {
        if (getResources(ResourceType.language, UserPreference.getLanguageCode()) instanceof LanguageResource language) {//尝试获取本土化翻译
            return language.getLanguageContent(key);
        } else if (getResources(ResourceType.language, packInfo.getString("defaultLanguageCode")) instanceof LanguageResource language) {
            return language.getLanguageContent(key);
        }
        throw new RuntimeException("没有语言");
    }


}
