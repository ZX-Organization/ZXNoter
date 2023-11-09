package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.config.ZXConfig;
import team.zxorg.zxnoter.resource.pack.LanguageResourcePack;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ResourcePack {
    /**
     * 唯一表示id (禁止使用'.' 用于分隔域)
     */
    private final String id;

    public Image getIcon() {
        return icon;
    }

    /**
     * 唯一表示id (禁止使用'.' 用于分隔域)
     */
    private final Image icon;
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

    private final HashMap<ResourceType, HashMap<String, BaseResourcePack>> allResources = new HashMap<>();

    public BaseResourcePack getResources(ResourceType type, String subId) {
        return allResources.get(type).get(subId);
    }

    public HashMap<String, BaseResourcePack> getResources(ResourceType type) {
        return allResources.get(type);
    }

    {
        //初始化所有资源map
        for (ResourceType type : ResourceType.values()) {
            allResources.put(type, new HashMap<>());
        }
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


        packPath = resourcePackPath;
        try {
            packInfo = JSON.parseObject(Files.newInputStream(resourcePackPath.resolve("resource-pack.json")));
            id = packInfo.getString("id");
            {
                Path iconPath = packPath.resolve(packInfo.getString("icon"));
                if (Files.exists(iconPath))
                    icon = new Image(packPath.resolve(packInfo.getString("icon")).toUri().toURL().toString());
                else icon = ZXResources.DEFAULT_RESOURCE_PACK_ICON;
            }
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
            BaseResourcePack baseResourcePack;
            for (int i = 0; i < resList.size(); i++) {
                try {
                    baseResourcePack = type.build(this, packPath.resolve(resList.getString(i)));
                    baseResourcePack.reload();
                    allResources.get(type).put(baseResourcePack.getResourceId(), baseResourcePack);
                } catch (Exception e) {
                    ZXLogger.warning("载入资源包 " + packPath.resolve(resList.getString(i)) + " 发生异常");
                }
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
        if (getResources(ResourceType.language, ZXConfig.configuration.userPreference.languageCode) instanceof LanguageResourcePack language) {//尝试获取本土化翻译
            return language.getLanguageContent(key);
        } else if (getResources(ResourceType.language, packInfo.getString("defaultLanguageCode")) instanceof LanguageResourcePack language) {
            return language.getLanguageContent(key);
        }
        throw new RuntimeException("没有语言 " + key);
    }


}
