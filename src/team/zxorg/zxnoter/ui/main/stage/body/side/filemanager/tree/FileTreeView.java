package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.tree;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.BaseFileView;

public class FileTreeView extends BaseFileView {
    TreeView<FileTreeItem> treeView = new TreeView<>();

    public FileTreeView() {
        VBox.setVgrow(treeView, Priority.ALWAYS);
        getChildren().add(treeView);
    }

    @Override
    protected void refresh() {
        treeView.setRoot(new TreeItem<>(new FileTreeItem()));
    }
}
