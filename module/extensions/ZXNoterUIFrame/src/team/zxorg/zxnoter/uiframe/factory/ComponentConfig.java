package team.zxorg.zxnoter.uiframe.factory;

import team.zxorg.zxnoter.uiframe.ZXNoter;

public class ComponentConfig {
    public static final ComponentConfig config;

     double menuIconSize;

    static {
        config = ZXNoter.config.get(ComponentConfig.class);
    }

    public ComponentConfig() {
        menuIconSize = 16;
    }
}
