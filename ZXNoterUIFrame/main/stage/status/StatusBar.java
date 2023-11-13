package team.zxorg.ui.main.stage.status;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.ui.main.ZXStage;
import team.zxorg.ui.component.ZXStatus;

public class StatusBar extends HBox {
    HBox infoHBox = new HBox();
    HBox editorStatusHBox = new HBox();
    HBox appStatusHBox = new HBox();


    public StatusBar(ZXStage zxStage) {
        getStyleClass().add("status-bar");

        infoHBox.getChildren().addAll(new ConsoleStatus());
        HBox.setHgrow(infoHBox, Priority.ALWAYS);
        getChildren().addAll(infoHBox, editorStatusHBox, appStatusHBox);

        zxStage.editorArea.focusTab.addListener((observable, oldValue, newValue) -> {
            editorStatusHBox.getChildren().clear();
            if (newValue != null)
                editorStatusHBox.getChildren().addAll(newValue.zxStatuses);
        });
        setHeight(26);
        setMinHeight(26);
        setMaxHeight(26);
    }

    public void addAppStatus(ZXStatus zxStatus) {
        appStatusHBox.getChildren().add(zxStatus);
    }
}
