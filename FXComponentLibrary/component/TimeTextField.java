package team.zxorg.ui.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import team.zxorg.ui.TimeUtils;


public class TimeTextField extends ZXGroupComponent {
    public LongProperty timeProperty = new SimpleLongProperty(-1);
    public LongProperty changeTimeProperty = new SimpleLongProperty(-1);
    BooleanProperty isFormatTime = new SimpleBooleanProperty(true);
    TextField textField = new TextField();
    ZXIconButton zxIconButton = new ZXIconButton("software.time-format", 20);

    public TimeTextField() {
        timeProperty.addListener((observable, oldValue, newValue) -> {
            if (isFormatTime.get()) {
                textField.setText(TimeUtils.formatTime(newValue.longValue()) + " s");
            } else {
                textField.setText(newValue.longValue() + " ms");
            }
        });
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String text = textField.getText().replaceAll("[a-zA-Z ]", "");
                if (isFormatTime.get()) {
                    changeTimeProperty.set(TimeUtils.parseTime(text));
                } else {
                    changeTimeProperty.set(Long.parseLong(text));
                }
                timeProperty.set(changeTimeProperty.get());
            }/*else {
                textField.setPrefColumnCount(textField.getText().length());
            }*/
        });
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                textField.getParent().requestFocus();
            }
        });

        zxIconButton.setOnAction(event -> {
            isFormatTime.set(!isFormatTime.get());
            if (isFormatTime.get()) {
                zxIconButton.setIconKey("software.time-format");
            } else {
                zxIconButton.setIconKey("software.time-ms");
            }
            timeProperty.set(timeProperty.get() + 1);
            timeProperty.set(timeProperty.get() - 1);
        });

        textField.setPrefWidth(80);
        textField.setAlignment(Pos.CENTER);
        getChildren().addAll(zxIconButton, textField);
        setSpacing(6);
        timeProperty.set(0);
    }


}
