package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.ui.component.ZXTextFieldGroup;
import team.zxorg.zxnoter.ui.main.stage.body.SideBar;
import team.zxorg.zxnoter.ui.main.stage.body.side.BaseSideBarTab;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.list.FileListView;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.tree.FileTreeView;

public class FileManagerTab extends BaseSideBarTab {
    VBox fileView = new VBox();

    public FileManagerTab(SideBar tabPane) {
        super("side-bar.menu.file-manager", "document.folder-4", tabPane);
        ZXTextFieldGroup searchGroup = new ZXTextFieldGroup("side-bar.menu.file-manager.search", "document.file-search");
        VBox.setVgrow(fileView, Priority.ALWAYS);
        body.setSpacing(6);
        body.getChildren().addAll(searchGroup, fileView);
        body.setPadding(new Insets(8));
        setFileViewType(1);
    }

    public void setFileViewType(int fileViewType) {
        fileView.getChildren().clear();
        BaseFileView newFileView = switch (fileViewType) {
            case 0 -> new FileTreeView();
            case 1 -> new FileListView();
            default -> null;
        };
        newFileView.refresh();
        fileView.getChildren().add(newFileView);
    }
}
