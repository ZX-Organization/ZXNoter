package team.zxorg.zxnoter.ui.base;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.zxorg.zxnoter.ui.component.activitypane.ActivityPaneSkin;

public class FileManagerActivityPaneSkin extends ActivityPaneSkin {

    public FileManagerActivityPaneSkin() {
        super("fileManager");
        getChildren().addAll(new Label("文件管理器"));
        setAlignment(Pos.CENTER);
    }
}
