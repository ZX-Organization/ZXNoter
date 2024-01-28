package team.zxorg.zxnoter.uiframe.factory;

import team.zxorg.zxnoter.uiframe.ZXNoterManager;

public class ComponentConfig {
    public static final ComponentConfig config;

     double menuIconSize;

    static {
        config = ZXNoterManager.config.get(ComponentConfig.class);
    }

    public ComponentConfig() {
        menuIconSize = 16;
    }
}
