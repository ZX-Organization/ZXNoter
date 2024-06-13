package team.zxorg.zxnoter.ui.base;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPaneSkin;

import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import static team.zxorg.extensionloader.core.Logger.simpleDateFormat;

public class LoggerActivityPaneSkin extends ActivityPaneSkin {
    public LoggerActivityPaneSkin() {
        super("logger");
        ListView<LoggerItem> listView = new ListView<>();
        getChildren().add(listView);


        Logger.addLoggerHandler(new StreamHandler() {
            @Override
            public void publish(LogRecord record) {
                listView.getItems().add(new LoggerItem(record));
                super.publish(record);
            }
        });
        setPadding(new Insets(6));
    }

    private static class LoggerItem extends HBox {
        Label time = new Label();
        Label level = new Label();
        Label info = new Label();

        public LoggerItem(LogRecord record) {
            time.setText(simpleDateFormat.format(Date.from(record.getInstant())));
            String levelString = record.getLevel().getLocalizedName();
            level.getStyleClass().add(switch (levelString) {
                case "SEVERE" -> "color-font-red";
                case "WARNING" -> "color-font-yellow";
                case "INFO" -> "color-font-white";
                case "DEBUG" -> "color-font-purple";
                default -> "";
            });
            level.setText(levelString);
            level.setMinWidth(50);
            level.setAlignment(Pos.CENTER);
            info.setText(": "+record.getMessage());
            getChildren().addAll(time, level, info);
        }
    }

}
