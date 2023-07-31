package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileManagerTab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public class FileTreeView extends BaseFileView {
    TreeView<FileItem> treeView = new TreeView<>();

    public FileTreeView(FileManagerTab fileManagerTab) {
        super(fileManagerTab);
        VBox.setVgrow(treeView, Priority.ALWAYS);
        getChildren().add(treeView);
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                if (newValue.getValue().isDirectory) {
                    folderSubItemCount.setValue(newValue.getChildren().size());
                } else {
                    folderSubItemCount.setValue(newValue.getParent().getChildren().size());
                }
        });
    }


    @Override
    public void refresh() {
        if (projectDirectoryPath.get() != null) {
            TreeItem<FileItem> root = new TreeItem<>(new FileItem(projectDirectoryPath.get(),zxProject));
            treeView.setRoot(root);
            updateSubTree(root);
            root.setExpanded(true);
            treeView.getSelectionModel().select(root);
        }
    }


    private void updateSubTree(TreeItem<FileItem> treeItem) {
        treeItem.getChildren().clear();
        FileItem fileItem = treeItem.getValue();
        try (Stream<Path> filesStream = Files.list(fileItem.path)) {
            Iterator<Path> files = filesStream.iterator();
            while (files.hasNext()) {
                Path subPath = files.next();
                TreeItem<FileItem> newTreeItem = new TreeItem<>(new FileItem(subPath,zxProject));
                treeItem.getChildren().add(newTreeItem);
                if (newTreeItem.getValue().isDirectory)
                    updateSubTree(newTreeItem);
            }
        } catch (IOException e) {
            ZXLogger.warning(e.getLocalizedMessage());
        }
    }
}
