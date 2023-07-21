package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.list;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.GlobalResources;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.*;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.BaseFileView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public class FileListView extends BaseFileView {
    ZXListView<FileListItem> fileListItemListView = new ZXListView<>();
    ObservableList<FileListItem> fileListItemList = fileListItemListView.getItems();
    Path currentRelativelyDirectory = Path.of("");
    Path selectedFile = null;
    Label showPath = new Label();
    ZXLabel showCount = new ZXLabel("side-bar.menu.file-manager.count", new SimpleObjectProperty<>(0));
    HBox controlTop = new HBox();
    HBox controlBottom = new HBox();

    {


        ZXIconButton backButton = new ZXIconButton("arrows.arrow-up", 16, "side-bar.menu.file-manager.back");
        backButton.setOnAction(event -> {
            selectedFile = projectDirectoryPath.resolve(currentRelativelyDirectory);
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

        ZXIconButton createFileButton = new ZXIconButton("document.file-add", 16, "side-bar.menu.file-manager.create-file");
        CreateFilePopupBox createFilePopupBox = new CreateFilePopupBox(createFileButton, false, TrackTooltip.BindAttributes.CLICK_POP_UP, TrackTooltip.BindAttributes.LOSE_FOCUS_DISAPPEAR);

        ZXIconButton createFolderButton = new ZXIconButton("document.folder-add", 16, "side-bar.menu.file-manager.create-folder");
        CreateFilePopupBox createFolderPopupBox = new CreateFilePopupBox(createFolderButton, true, TrackTooltip.BindAttributes.CLICK_POP_UP, TrackTooltip.BindAttributes.LOSE_FOCUS_DISAPPEAR);


        ZXIconButton menuButton = new ZXIconButton("system.menu", 16, "side-bar.menu.file-manager.style");
        TrackPopupVHBox menuPopupBox = new TrackPopupVHBox(menuButton, Pos.TOP_CENTER, 0);
        menuPopupBox.setAlignment(Pos.CENTER);
        menuPopupBox.getChildren().addAll(new TextField());
        //menuButton.setOnAction((event -> menuProgress.showTrackTooltip()));


        HBox.setHgrow(showCount, Priority.ALWAYS);
        showCount.setMaxWidth(Double.MAX_VALUE);
        controlBottom.setAlignment(Pos.CENTER_LEFT);
        controlBottom.setPadding(new Insets(2, 4, 2, 4));
        controlBottom.setSpacing(4);
        controlBottom.getChildren().addAll(showCount, createFileButton, createFolderButton, menuButton);
        controlBottom.getStyleClass().add("border-color-top");

        getChildren().addAll(controlTop, fileListItemListView, controlBottom);
        VBox.setVgrow(fileListItemListView, Priority.ALWAYS);
        //fileListItemListView.setFixedCellSize(40);
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
            int count = 0;
            while (files.hasNext()) {
                Path file = files.next();
                count++;
                FileListItem fileListItem = new FileListItem(file);
                fileListItem.setOnMouseClicked(itemMouseClickEvent);
                fileListItemList.add(fileListItem);
                if (selectedFile != null) {
                    if (selectedFile.equals(file)) {
                        fileListItemListView.getSelectionModel().select(fileListItem);
                        fileListItemListView.scrollTo(fileListItem);
                    }
                }
            }
            showCount.getProperty(0).set(count);
        } catch (IOException e) {
            ZXLogger.warning("枚举文件目录时异常");
        }
    }
}
