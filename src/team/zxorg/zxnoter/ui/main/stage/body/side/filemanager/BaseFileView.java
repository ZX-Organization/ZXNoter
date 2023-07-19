package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.UserPreference;

import java.nio.file.Path;

public abstract class BaseFileView extends VBox {
    /**
     * 项目根目录
     */
    protected Path projectDirectoryPath = UserPreference.getProjectDirectory();

    {
        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("file-view");
    }

    public BaseFileView() {

    }

    protected abstract void refresh();
}
