package team.zxorg.zxnoter.ui.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.pack.BaseResourcePack;

import java.util.ArrayList;

public class ZXLabel extends Label {
    public ObjectProperty<ZXColor> color = new SimpleObjectProperty<>();
    public StringProperty langKey = new SimpleStringProperty();
    public TrackTooltip tooltip = new TrackTooltip(this, Pos.TOP_CENTER, 0);//工具提示
    public StringProperty languageContent = new SimpleStringProperty();
    public ArrayList<StringProperty> properties = new ArrayList<>();

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
            getStyleClass().filtered(s -> s.contains("text-")).clear();
            getStyleClass().add("text-" + newValue);
        });
        languageContent.addListener((observable, oldValue, newValue) -> {
            setText(String.format(newValue, properties));
        });
    }

    public ZXLabel(String langKey, ZXColor color) {
        setColor(color);
        setLangKey(langKey);
    }

    public ZXLabel(String langKey) {
        setColor(ZXColor.FONT_USUALLY);
        setLangKey(langKey);
    }

    public void setColor(ZXColor zxColor) {
        color.set(zxColor);
    }

    public void setLangKey(String langKey) {
        this.langKey.set(langKey);
    }
}
