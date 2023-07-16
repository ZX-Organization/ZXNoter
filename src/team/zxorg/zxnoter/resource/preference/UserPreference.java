package team.zxorg.zxnoter.resource.preference;

import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.resource.ZXConfiguration;

import java.util.ArrayList;

public class UserPreference {
    private static final JSONObject config = ZXConfiguration.getScopeConfig("userPreference");

    public static String getLanguageCode() {
        return config.getString("languageCode");
    }

    /**
     * 获取启用的资源列表
     *
     * @return 对应的样式id
     */
    public static String[] getUsedResources() {
        return config.getObject("usedResources", String[].class);
    }
}
