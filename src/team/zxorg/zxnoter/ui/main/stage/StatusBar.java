package team.zxorg.zxnoter.ui.main.stage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.ui.component.ZXStatus;
import team.zxorg.zxnoter.ui.main.ZXStage;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorTabPane;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.base.BaseTab;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class StatusBar extends HBox {
    HBox infoHBox = new HBox();
    HBox editorStatusHBox = new HBox();
    HBox appStatusHBox = new HBox();

    public StatusBar(ZXStage zxStage) {
        getStyleClass().add("status-bar");
        Label info = new Label();
        ZXLogger.addLoggerHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (record.getLevel().equals(Level.SEVERE))
                    info.setText(record.getMessage());
                if (record.getLevel().equals(Level.WARNING))
                    info.setText(record.getMessage());
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        });
        infoHBox.getChildren().addAll(info);
        HBox.setHgrow(infoHBox, Priority.ALWAYS);
        getChildren().addAll(infoHBox, editorStatusHBox, appStatusHBox);

        zxStage.editorArea.focusTab.addListener((observable, oldValue, newValue) -> {
            editorStatusHBox.getChildren().clear();
            if (newValue != null)
                editorStatusHBox.getChildren().addAll(newValue.zxStatuses);
        });
    }

    public void addAppStatus(ZXStatus zxStatus) {
        appStatusHBox.getChildren().add(zxStatus);
    }
}
