package team.zxorg.zxnoter.uiframe.factory;

import team.zxorg.fxcl.component.Icon;

public class IconFactory {
    public static Icon getMenuIcon(String key) {
        return new Icon(key, ComponentConfig.config.menuIconSize);
    }
}
