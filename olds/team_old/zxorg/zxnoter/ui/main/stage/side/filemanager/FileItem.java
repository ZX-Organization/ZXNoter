package team.zxorg.zxnoter.ui.main.stage.side.filemanager;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import team.zxorg.zxnoter.resource.ZXFileType;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.main.ZXStage;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileItem extends HBox {
    public final Path path;
    public final Label fileName = new Label();
    public final boolean isDirectory;
    public final ZXFileType fileType;

    @Override
    public String toString() {
        return (isDirectory ? "文件夹" : "文件") + ":" + path + " " + fileType.type + " " + fileType.extensionName;
    }

    public FileItem(Path path, ZXStage zxStage) {
        this.path = path;
        isDirectory = Files.isDirectory(path);
        fileName.setText(String.valueOf(path.getFileName()));
        fileType = ZXIcon.getFileType(path);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(4);
        //setPrefHeight(32);
        fileName.setWrapText(true);
        fileName.setPrefWidth(Region.USE_COMPUTED_SIZE);

        HBox.setHgrow(fileName, Priority.ALWAYS);
        fileName.setPrefHeight(Region.USE_COMPUTED_SIZE);
        getChildren().addAll(new ZXIcon(fileType, 16), fileName);

        setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY)) {
                if (!isDirectory)
                    zxStage.editorArea.openFile(this);
            }
        });

        setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                FileMenu fileMenu = new FileMenu();
                fileMenu.show(getScene().getWindow(), event.getScreenX(), event.getScreenY());
            }
        });
    }

}
