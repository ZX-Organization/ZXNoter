package team.zxorg.zxnoter.ui.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import team.zxorg.zxnoter.resource.ZXColor;

public class ZXVolumeIconButton extends ZXIconButton {
    public IntegerProperty volume = new SimpleIntegerProperty(30);
    int lastVolume = 0;
    TrackPopupVHBox vhBox = new TrackPopupVHBox(Orientation.VERTICAL, this, Pos.TOP_CENTER, 0, TrackTooltip.BindAttributes.NOT_DISAPPEAR, TrackTooltip.BindAttributes.LOSE_FOCUS_DISAPPEAR, TrackTooltip.BindAttributes.HOVER_EXIT_DISAPPEAR);
    Slider volumeSlider = new Slider(0, 100, 20);
    Label label = new Label("20%");

    public ZXVolumeIconButton() {
        super("media.volume-down", 20);
        label.setPrefWidth(36);
        label.setAlignment(Pos.CENTER);
        volume.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 0) {
                setIconKey("media.volume-mute");
            } else if (newValue.intValue() < 50) {
                setIconKey("media.volume-down");
            } else if (newValue.intValue() < 100) {
                setIconKey("media.volume-up");
            }
        });
        addEventFilter(ScrollEvent.SCROLL, event -> {
            setVolume(volume.get() + (event.getDeltaY() > 0 ? 10 : -10));
            event.consume();
        });
        volumeSlider.setPrefHeight(100);
        volumeSlider.setOrientation(Orientation.VERTICAL);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(newValue.intValue() + "%");
        });
        volume.bindBidirectional(volumeSlider.valueProperty());

        vhBox.getChildren().addAll(volumeSlider, label);
        vhBox.setAlignment(Pos.TOP_CENTER);
        volumeSlider.setPadding(new Insets(8, 0, 4, 0));
        setOnMouseEntered((event -> {
            vhBox.showTrackTooltip();
        }));
        setOnMouseExited((event -> {
            if (event.getY() > 1)
                vhBox.hide();
        }));
        setOnAction(event -> {
            if (volume.get() == 0) {
                setVolume(lastVolume);
            } else {
                lastVolume = volume.get();
                setVolume(0);
            }
        });
    }

    public void setVolume(int value) {
        volumeSlider.setValue(Math.max(Math.min(value, 100), 0));
    }

}
