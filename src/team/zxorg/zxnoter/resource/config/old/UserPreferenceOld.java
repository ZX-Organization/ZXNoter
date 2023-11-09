package team.zxorg.zxnoter.resource.config.old;

import com.alibaba.fastjson2.JSONObject;
import javafx.beans.property.SimpleBooleanProperty;
import team.zxorg.zxnoter.resource.ResourceType;

import java.nio.file.Path;

public class UserPreferenceOld {
    //private static final JSONObject config = ZXConfig.getScopeConfig("userPreference");



    public static final SimpleBooleanProperty isCloseAutoSave = new SimpleBooleanProperty();//关闭时自动保存文件


    public static String getLanguageCode() {
        return config.getString("languageCode");
    }

    /**
     * 设置语言代码
     *
     * @param languageCode 语言代码
     */
    public static void setLanguageCode(String languageCode) {
        config.put("languageCode", languageCode);
    }


    /**
     * 获取基本资源
     *
     * @param type 类型
     * @return 资源
     */
    public static String getUsedBaseResources(ResourceType type) {
        return config.getJSONObject("usedResources").getJSONObject(type.name()).getString("base");
    }

    /**
     * 获取使用的附加资源
     *
     * @param type 类型
     * @return 资源fullId
     */
    public static String[] getUsedAttachResources(ResourceType type) {
        return config.getJSONObject("usedResources").getJSONObject(type.name()).getObject("attach", String[].class);
    }

    public static void setUsedBaseResources(ResourceType type, String fullId) {
        config.getJSONObject("usedResources").getJSONObject(type.name()).put("base", fullId);
    }

    public static Path getProjectDirectory() {
        return Path.of(config.getString("projectDirectory"));
    }

    public static JSONObject getSideConfiguration(String sideKey) {
        return config.getJSONObject("side-bar").getJSONObject(sideKey);
    }
}
