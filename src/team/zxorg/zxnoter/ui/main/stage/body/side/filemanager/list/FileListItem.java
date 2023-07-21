package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.list;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import team.zxorg.zxnoter.ui.component.ZXFileIcon;
import team.zxorg.zxnoter.ui.component.ZXIcon;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileListItem extends HBox {
    Path path;
    Label fileName = new Label();
    boolean isDirectory;
    ZXFileIcon icon;

    public FileListItem(Path path) {
        this.path = path;
        isDirectory = Files.isDirectory(path);
        fileName.setText(String.valueOf(path.getFileName()));
        icon = ZXIcon.getFileIcon(path);
        setAlignment(Pos.CENTER_LEFT);
        // setPadding(new Insets(0, 4, 0, 4));
        setSpacing(4);
        setPrefHeight(32);
        fileName.setWrapText(true);
        fileName.setPrefWidth(Region.USE_COMPUTED_SIZE);

        HBox.setHgrow(fileName, Priority.ALWAYS);
        fileName.setPrefHeight(Region.USE_COMPUTED_SIZE);
        getChildren().addAll(new ZXIcon(icon, 16), fileName);
        /*parentProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue!=null){
                if (newValue instanceof Region region){
                    System.out.println(region);
                    System.out.println(region.getPrefWidth());
                }
            }
        });*/
    }
}
