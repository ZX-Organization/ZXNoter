package team.zxorg.zxnoter.ui.main.stage.side.filemanager.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.ui.main.stage.side.filemanager.FileManagerTab;

import java.nio.file.Path;

public abstract class BaseFileView extends VBox {
    /**
     * 项目根目录
     */
    protected ObjectProperty<Path> projectPath;
    protected ObjectProperty<Integer> folderSubItemCount;


    {
        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("file-view");
    }


    public BaseFileView(FileManagerTab fileManagerTab) {
        this.folderSubItemCount = fileManagerTab.showCountProperty;
        projectPath = fileManagerTab.zxStage.zxProject.openedPath;
        projectPath.addListener((observable, oldValue, newValue) -> {
            refresh();
        });
    }

    public abstract void refresh();
}
