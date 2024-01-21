package team.zxorg.fxcl.component;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import team.zxorg.fxcl.resource.LanguageManager;

import java.text.MessageFormat;

public class LangLabel extends Label {
    ObjectProperty<MessageFormat> messageFormat;
    Property<?>[] args;

    public LangLabel(String key, Property<?>... args) {
        messageFormat = LanguageManager.getLanguage(key);
        this.args = args;
        InvalidationListener updateListener = observable -> {
            Object[] fmArgs = new Object[args.length];
            for (int i = 0; i < fmArgs.length; i++)
                fmArgs[i] = args[i].getValue();
            setText(messageFormat.get().format(fmArgs));
        };
        messageFormat.addListener(updateListener);
        for (Property<?> arg : args)
            arg.addListener(updateListener);
        updateListener.invalidated(null);
    }
}
