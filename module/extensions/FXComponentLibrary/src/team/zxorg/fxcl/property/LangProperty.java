package team.zxorg.fxcl.property;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyStringWrapper;
import team.zxorg.fxcl.resource.LanguageManager;

import java.text.MessageFormat;

public class LangProperty extends ReadOnlyStringWrapper {

    ObjectProperty<MessageFormat> messageFormat;
    Property<?>[] args;

    public LangProperty() {
    }

    public LangProperty(String key, Property<?>... args) {
        setLang(key, args);
    }


    private final InvalidationListener updateListener = observable -> {
        Object[] fmArgs = new Object[args.length];
        for (int i = 0; i < fmArgs.length; i++)
            fmArgs[i] = args[i].getValue();
        set(messageFormat.get().format(fmArgs));
    };

    public void setLang(Object key, Property<?>... args) {
        setLang(key.toString(), args);
    }

    public void setLang(String key, Property<?>... args) {
        if (messageFormat != null) {
            messageFormat.removeListener(updateListener);
        }

        if (args != null) {
            for (Property<?> arg : args)
                arg.removeListener(updateListener);
        }

        messageFormat = LanguageManager.getLanguage(key);
        this.args = args;

        messageFormat.addListener(updateListener);
        for (Property<?> arg : args)
            arg.addListener(updateListener);
        updateListener.invalidated(null);
    }
}
