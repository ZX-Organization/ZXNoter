package team.zxorg.zxnoter.ui.base;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPane;

public class FileManagerActivityPane extends ActivityPane {

    public FileManagerActivityPane() {
        super("fileManager");
        getChildren().addAll(new Label("文件管理器"));
        setAlignment(Pos.CENTER);
    }
}
