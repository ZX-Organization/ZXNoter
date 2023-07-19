package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.list;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.ui.component.ZXIconButton;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.BaseFileView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public class FileListView extends BaseFileView {
    ListView<FileListItem> fileListItemListView = new ListView<>();
    ObservableList<FileListItem> fileListItemList = fileListItemListView.getItems();
    Path currentRelativelyDirectory = Path.of("");
    Label showPath = new Label();
    HBox control = new HBox();

    {
        control.setAlignment(Pos.CENTER_LEFT);
        control.setPadding(new Insets(4));
        ZXIconButton backButton = new ZXIconButton("arrows.arrow-up", 16, "side-bar.menu.file-manager.back");
        backButton.setOnAction(event -> {
            currentRelativelyDirectory = currentRelativelyDirectory.getParent();
            if (currentRelativelyDirectory == null)
                currentRelativelyDirectory = Path.of("");
            refresh();
        });
        control.setSpacing(6);
        control.getChildren().addAll(backButton, showPath);
        control.getStyleClass().add("border-color-bottom");
        getChildren().addAll(control, fileListItemListView);
        VBox.setVgrow(fileListItemListView, Priority.ALWAYS);
    }

    EventHandler<MouseEvent> itemMouseClickEvent = event -> {
        if (event.getClickCount() == 2) {
            if (event.getSource() instanceof FileListItem fileListItem) {
                if (fileListItem.isDirectory) {
                    currentRelativelyDirectory = currentRelativelyDirectory.resolve(fileListItem.path.getFileName());
                    refresh();
                }
            }
        }
    };

    @Override
    protected void refresh() {
        fileListItemList.clear();
        Path currentDirectory = projectDirectoryPath.resolve(currentRelativelyDirectory);
        showPath.setText(String.valueOf(currentRelativelyDirectory));
        try (Stream<Path> filesStream = Files.list(currentDirectory)) {
            Iterator<Path> files = filesStream.iterator();
            while (files.hasNext()) {
                FileListItem fileListItem = new FileListItem(files.next());
                fileListItem.setOnMouseClicked(itemMouseClickEvent);
                fileListItemList.add(fileListItem);
            }
        } catch (IOException e) {
            ZXLogger.warning("枚举文件目录时异常");
        }
    }
}
