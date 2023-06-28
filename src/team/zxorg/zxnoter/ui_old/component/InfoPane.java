package team.zxorg.zxnoter.ui_old.component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import team.zxorg.zxnoter.map.mapInfo.UnLocalizedMapInfo;
import team.zxorg.zxnoter.map.mapInfo.ZXMInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoPane extends HBox {
    UnLocalizedMapInfo unlocalizedInfo;


    public InfoPane(UnLocalizedMapInfo unlocalizedInfo, ZXMInfo[] infos) {
        this.unlocalizedInfo = unlocalizedInfo;
        for (ZXMInfo info : infos) {
            InfoEntry infoEntry = new InfoEntry(info);

            getChildren().add(infoEntry);
        }
    }

    private class InfoEntry extends HBox {
        ZXMInfo zxmInfo;
        TextField valueTextField = new TextField();

        public InfoEntry(ZXMInfo zxmInfo) {
            this.zxmInfo = zxmInfo;

            Label label = new Label(zxmInfo.name());
            getChildren().addAll(label, valueTextField);
            valueTextField.setText(unlocalizedInfo.getInfo(zxmInfo));
            valueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                unlocalizedInfo.addInfo(zxmInfo, newValue);
            });

        }
    }

}
