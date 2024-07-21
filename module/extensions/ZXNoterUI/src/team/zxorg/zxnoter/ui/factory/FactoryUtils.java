package team.zxorg.zxnoter.ui.factory;

import team.zxorg.extensionloader.core.Language;
import team.zxorg.fxcl.component.Icon;

import java.util.Objects;

public class FactoryUtils {
    public static String getKey(String key) {
        return (Language.getOrNull(key) == null ? key + ".name" : key);
    }

    public static Icon getIcon(String key, double size) {
        String iconKey = Language.getOrNull(key + ".icon");
        if (iconKey == null)
            return null;
        size = Double.parseDouble(Objects.requireNonNullElse(Language.getOrNull(key + ".size"), String.valueOf(size)));
        return new Icon(iconKey, size);
    }
}
