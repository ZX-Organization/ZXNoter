package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;

import java.util.ArrayList;

public class ZXButton extends Button {
    private final ObjectProperty<ZXColor> color = new SimpleObjectProperty<>();
    private final StringProperty langKey = new SimpleStringProperty();
    private final TrackTooltip tooltip = new TrackTooltip(this, Pos.TOP_CENTER, 0);//工具提示
    private final StringProperty languageContent = new SimpleStringProperty();
    private final ArrayList<ObjectProperty<?>> properties = new ArrayList<>();

    {
        langKey.addListener((observable, oldValue, newValue) -> {
            languageContent.bind(GlobalResources.getLanguageContent(newValue));
            StringProperty tipString = GlobalResources.getLanguageContent(newValue + ".tip");
            if (tipString != null) {
                tooltip.textProperty().bind(tipString);
                Tooltip.install(this, tooltip);
            } else {
                Tooltip.uninstall(this, tooltip);
            }
        });
        color.addListener((observable, oldValue, newValue) -> {
            getStyleClass().removeAll(getStyleClass().filtered(s -> s.contains("button-")));
            getStyleClass().add("button-" + newValue);
        });
        languageContent.addListener((observable, oldValue, newValue) -> {
            update(newValue);
        });
    }

    private void update(String newValue) {
        if (newValue.contains("%")) {
            int count = newValue.length() - newValue.replaceAll("%", "").length();
            if (count > properties.size()) {
                return;
            }
            Object[] objects = new Object[properties.size()];
            for (int i = 0; i < properties.size(); i++) {
                objects[i] = properties.get(i).get();
            }
            setText(String.format(newValue, objects));
            return;
        }
        setText(newValue);
    }

    public ZXButton(String langKey, ZXColor color) {
        setColor(color);
        setLangKey(langKey);
    }

    public ZXButton(String langKey) {
        setColor(ZXColor.FONT_USUALLY);
        setLangKey(langKey);
    }

    public ZXButton(String langKey, ObjectProperty... properties) {
        setColor(ZXColor.FONT_USUALLY);
        setLangKey(langKey);
        for (ObjectProperty property : properties) {
            this.properties.add(property);
            property.addListener((observable, oldValue, newValue) -> {
                update(languageContent.getValue());
            });
        }
    }

    public ObjectProperty getProperty(int index) {
        if (index >= properties.size())
            throw new IllegalArgumentException("下标异常");
        return properties.get(index);
    }

    public void setColor(ZXColor zxColor) {
        color.set(zxColor);
    }

    public void setLangKey(String langKey) {
        this.langKey.set(langKey);
    }
}
