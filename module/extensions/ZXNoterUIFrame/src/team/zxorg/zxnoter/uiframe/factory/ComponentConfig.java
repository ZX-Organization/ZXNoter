package team.zxorg.zxnoter.uiframe.factory;

import team.zxorg.zxnoter.uiframe.ZXNoter;

public class ComponentConfig {
    public static final ComponentConfig config;

    static {
        config = ZXNoter.config.get(ComponentConfig.class);
    }

}
