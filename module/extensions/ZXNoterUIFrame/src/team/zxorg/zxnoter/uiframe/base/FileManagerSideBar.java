package team.zxorg.zxnoter.uiframe.base;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.uiframe.component.SideBar;

public class FileManagerSideBar extends SideBar {
    {
        getChildren().addAll(new Label("文件管理器"));
        setAlignment(Pos.CENTER);
    }
}
