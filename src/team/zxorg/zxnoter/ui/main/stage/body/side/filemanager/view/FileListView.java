package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.ui.component.*;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileManagerTab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public class FileListView extends BaseFileView {
    ZXListView<FileItem> fileListItemListView = new ZXListView<>();
    ObservableList<FileItem> fileListItemList = fileListItemListView.getItems();
    Path currentRelativelyDirectory = Path.of("");
    Path selectedFile = null;
    Label showPath = new Label();
    HBox controlTop = new HBox();

    {


        ZXIconButton backButton = new ZXIconButton("arrows.arrow-up", 16, "side-bar.file-manager.back");
        backButton.setOnAction(event -> {
            selectedFile = projectDirectoryPath.get().resolve(currentRelativelyDirectory);
            currentRelativelyDirectory = currentRelativelyDirectory.getParent();
            if (currentRelativelyDirectory == null)
                currentRelativelyDirectory = Path.of("");
            refresh();

        });

        HBox.setHgrow(showPath, Priority.ALWAYS);
        showPath.setPrefWidth(Region.USE_COMPUTED_SIZE);
        controlTop.setAlignment(Pos.CENTER_LEFT);
        controlTop.setPadding(new Insets(2, 4, 2, 4));
        controlTop.setSpacing(4);
        controlTop.getChildren().addAll(backButton, showPath);
        controlTop.getStyleClass().add("border-color-bottom");


        ZXIconButton menuButton = new ZXIconButton("system.menu", 16, "side-bar.file-manager.style");
        TrackPopupVHBox menuPopupBox = new TrackPopupVHBox(menuButton, Pos.TOP_CENTER, 0);
        menuPopupBox.setAlignment(Pos.CENTER);
        menuPopupBox.getChildren().addAll(new TextField());
        //menuButton.setOnAction((event -> menuProgress.showTrackTooltip()));


        getChildren().addAll(controlTop, fileListItemListView);
        VBox.setVgrow(fileListItemListView, Priority.ALWAYS);
        //fileListItemListView.setFixedCellSize(40);
    }

    EventHandler<MouseEvent> itemMouseClickEvent = event -> {
        if (event.getClickCount() == 2) {
            if (event.getSource() instanceof FileItem fileListItem) {
                if (fileListItem.isDirectory) {
                    currentRelativelyDirectory = currentRelativelyDirectory.resolve(fileListItem.path.getFileName());
                    refresh();
                }
            }
        }
    };

    public FileListView(FileManagerTab fileManagerTab) {
        super(fileManagerTab);
    }


    @Override
    public void refresh() {
        fileListItemList.clear();
        Path currentDirectory = projectDirectoryPath.get().resolve(currentRelativelyDirectory);
        showPath.setText(String.valueOf(currentRelativelyDirectory));
        try (Stream<Path> filesStream = Files.list(currentDirectory)) {
            Iterator<Path> files = filesStream.iterator();
            int count = 0;
            while (files.hasNext()) {
                Path file = files.next();
                count++;
                FileItem fileListItem = new FileItem(file);
                fileListItem.setOnMouseClicked(itemMouseClickEvent);
                fileListItemList.add(fileListItem);
                if (selectedFile != null) {
                    if (selectedFile.equals(file)) {
                        fileListItemListView.getSelectionModel().select(fileListItem);
                        fileListItemListView.scrollTo(fileListItem);
                    }
                }
            }
            folderSubItemCount.setValue(count);
        } catch (IOException e) {
            ZXLogger.warning("枚举文件目录时异常");
        }
    }
}
