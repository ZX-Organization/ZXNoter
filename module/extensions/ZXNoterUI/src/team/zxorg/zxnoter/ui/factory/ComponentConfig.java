package team.zxorg.zxnoter.ui.factory;

import team.zxorg.zxnoter.ui.ZXNoter;

public class ComponentConfig {
    public static final ComponentConfig config;

    static {
        config = ZXNoter.config.get(ComponentConfig.class);
    }

}
