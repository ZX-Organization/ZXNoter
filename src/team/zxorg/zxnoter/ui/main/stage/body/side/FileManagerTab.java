package team.zxorg.zxnoter.ui.main.stage.body.side;

import javafx.geometry.Insets;
import team.zxorg.zxnoter.ui.component.ZXTextFieldGroup;
import team.zxorg.zxnoter.ui.main.stage.body.SideBar;

public class FileManagerTab extends BaseSideBarTab {

    public FileManagerTab(SideBar tabPane) {
        super("side-bar.menu.file-manager", "document.folder-4", tabPane);
        ZXTextFieldGroup searchGroup = new ZXTextFieldGroup("side-bar.menu.file-manager.search", "system.search");
        body.getChildren().addAll(searchGroup);
        body.setPadding(new Insets(8, 8, 0, 8));
    }
}
