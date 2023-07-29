package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import team.zxorg.zxnoter.ui.component.ZXFileIcon;
import team.zxorg.zxnoter.ui.component.ZXIcon;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileItem extends HBox {
    public final Path path;
    public final Label fileName = new Label();
    public final boolean isDirectory;
    public final ZXFileIcon icon;

    public FileItem(Path path) {
        this.path = path;
        isDirectory = Files.isDirectory(path);
        fileName.setText(String.valueOf(path.getFileName()));
        icon = ZXIcon.getFileIcon(path);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(4);
        //setPrefHeight(32);
        fileName.setWrapText(true);
        fileName.setPrefWidth(Region.USE_COMPUTED_SIZE);

        HBox.setHgrow(fileName, Priority.ALWAYS);
        fileName.setPrefHeight(Region.USE_COMPUTED_SIZE);
        getChildren().addAll(new ZXIcon(icon, 16), fileName);
    }
}
