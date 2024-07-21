package team.zxorg.zxnoter.ui.factory;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import team.zxorg.fxcl.component.Icon;
import team.zxorg.fxcl.component.LangAlert;

import java.util.Objects;

import static team.zxorg.zxnoter.ui.ProjectView.globalStylesheets;

public class AlertFactory {
    private static final double DEFAULT_ICON_SIZE = 30;

    public static LangAlert getAlert(Alert.AlertType type, String key, ButtonType... buttonTypes) {
        LangAlert langAlert = new LangAlert(type);
        langAlert.getButtonTypes().addAll(buttonTypes);
        Icon icon = FactoryUtils.getIcon(key, DEFAULT_ICON_SIZE);
        if (Objects.nonNull(icon))
            langAlert.setIcon(icon);
        Bindings.bindContent(langAlert.getDialogPane().getScene().getStylesheets(), globalStylesheets);


        // 获取现有的头部内容
        // 创建新的头部布局



        langAlert.setHeaderLang(key + ".header");
        langAlert.setTitleLang(key + ".title");
        langAlert.setContentLang(key + ".content");


        return langAlert;
    }
}
