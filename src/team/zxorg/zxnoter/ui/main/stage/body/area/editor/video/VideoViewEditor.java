package team.zxorg.zxnoter.ui.main.stage.body.area.editor.video;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.TimeUtils;
import team.zxorg.zxnoter.ui.component.TimeTextField;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.component.ZXIconButton;
import team.zxorg.zxnoter.ui.component.ZXVolumeIconButton;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.base.BaseFileEditor;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;

public class VideoViewEditor extends BaseFileEditor {
    Media media;
    MediaPlayer mediaPlayer;
    MediaView mediaView;
    TimeTextField timeTextField = new TimeTextField();

    ZXVolumeIconButton volumeIconButton = new ZXVolumeIconButton();

    Slider timeSlider = new Slider();
    Label stopTimeLabel = new Label();

    public VideoViewEditor(FileItem fileItem, ZXProject zxProject) {
        super(fileItem, zxProject);
        isEditable.set(false);//不可编辑
        media = new Media(fileItem.path.toUri().toString());
        media.setOnError(() -> {
            ZXLogger.warning("载入媒体时发生异常" + media.getError());
            close();
        });
        mediaPlayer = new MediaPlayer(media);
        mediaView = new MediaView(mediaPlayer);
        body.setCenter(mediaView);

        ZXIconButton zxIconButton = new ZXIconButton("media.play", 22);

        mediaPlayer.setOnError(() -> {
            ZXLogger.warning("媒体播放器发生异常 " + mediaPlayer.getError());
            close();
        });

        mediaPlayer.setOnReady(() -> {

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                timeTextField.timeProperty.set((long) newValue.toMillis());
                timeSlider.setValue(timeTextField.timeProperty.getValue());
            });
            timeTextField.changeTimeProperty.addListener((observable, oldValue, newValue) -> mediaPlayer.seek(new Duration(newValue.longValue() + 1)));
            timeSlider.setMax(mediaPlayer.getStopTime().toMillis());
            stopTimeLabel.setText(TimeUtils.formatTime((long) timeSlider.getMax()));
            mediaPlayer.statusProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals(MediaPlayer.Status.PLAYING)) {
                    zxIconButton.setIconKey("media.pause");
                } else {
                    zxIconButton.setIconKey("media.play");
                }
            });

        });

        timeSlider.setOnMouseDragged((event) -> {
            timeTextField.changeTimeProperty.set((long) timeSlider.getValue());
        });


        volumeIconButton.volume.addListener((observable, oldValue, newValue) -> {
            mediaPlayer.volumeProperty().set(newValue.intValue() / 100d);
        });


        zxIconButton.setOnAction(event -> {
            //mediaPlayer.setRate(0.8);
            if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }

        });

        HBox toolbar = new HBox(zxIconButton, volumeIconButton, timeTextField, timeSlider, stopTimeLabel);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setSpacing(8);
        toolbar.setPadding(new Insets(8));
        body.setTop(toolbar);
    }

    @Override
    protected void saveFile() {

    }

    @Override
    protected void closed() {
        mediaPlayer.stop();
    }
}
