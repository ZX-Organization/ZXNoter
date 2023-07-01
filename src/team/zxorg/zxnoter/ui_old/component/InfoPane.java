package team.zxorg.zxnoter.ui_old.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.info.map.UnLocalizedMapInfo;
import team.zxorg.zxnoter.info.map.ZXMInfo;

import java.util.HashMap;

public class InfoPane extends VBox {
    UnLocalizedMapInfo unlocalizedInfo;
    HashMap<ZXMInfo, InfoEntry> children = new HashMap<>();

    public InfoPane(UnLocalizedMapInfo unlocalizedInfo, ZXMInfo[] infos) {
        this.unlocalizedInfo = unlocalizedInfo;
        setAlignment(Pos.TOP_CENTER);
        setSpacing(2);
        unlocalizedInfo.addListener((info, value) -> {
            InfoEntry infoEntry = children.get(info);
            if (infoEntry != null)
                infoEntry.change(value);
        });

        for (ZXMInfo info : infos) {
            InfoEntry infoEntry = new InfoEntry(info);
            children.put(info, infoEntry);
            VBox.setMargin(infoEntry, new Insets(0, 4, 0, 4));
            getChildren().add(infoEntry);
        }

    }


    private class InfoEntry extends HBox {
        ZXMInfo zxmInfo;
        TextField valueTextField = new TextField();

        public InfoEntry(ZXMInfo zxmInfo) {
            this.zxmInfo = zxmInfo;
            setAlignment(Pos.CENTER_RIGHT);
            setSpacing(2);
            Label label = new Label(zxmInfo.name());
            label.setMinWidth(110);
            label.setMaxWidth(800);
            label.setTextFill(Color.WHEAT);
            getChildren().addAll(label, valueTextField);
            valueTextField.setMinWidth(80);
            valueTextField.setMaxWidth(400);
            valueTextField.setText(unlocalizedInfo.getInfo(zxmInfo));
            valueTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                unlocalizedInfo.setInfo(zxmInfo, newValue);
            });

        }

        public void change(String v) {
            valueTextField.setText(v);
        }


    }

}
