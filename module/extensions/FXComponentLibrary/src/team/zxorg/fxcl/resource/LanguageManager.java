package team.zxorg.fxcl.resource;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.event.LanguageEventListener;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    private static final HashMap<String, ObjectProperty<MessageFormat>> fxLanguageMap = new HashMap<>();

    static {
        for (Map.Entry<String, MessageFormat> entry : Language.getLanguageMap().entrySet()) {
            fxLanguageMap.put(entry.getKey(), new SimpleObjectProperty<>(entry.getValue()));
        }
        Language.addEventListener(new LanguageEventListener() {
            @Override
            public void onLanguageChange(String key, MessageFormat language) {
                fxLanguageMap.get(key).set(language);
            }

            @Override
            public void onLanguageRemoved(String key, MessageFormat language) {
                fxLanguageMap.remove(key);
            }

            @Override
            public void onLanguageAdded(String key, MessageFormat language) {
                fxLanguageMap.put(key, new SimpleObjectProperty<>(language));
            }
        });
    }

    public static ObjectProperty<MessageFormat> getLanguage(String key) {
        return fxLanguageMap.getOrDefault(key, new SimpleObjectProperty<>(new MessageFormat(Language.get("message.language.lost", key))));
    }

}
