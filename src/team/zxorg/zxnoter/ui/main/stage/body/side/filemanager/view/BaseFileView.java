package team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.resource.UserPreference;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileManagerTab;

import java.nio.file.Path;

public abstract class BaseFileView extends VBox {
    /**
     * 项目根目录
     */
    protected ObjectProperty<Path> projectDirectoryPath;
    protected ObjectProperty<Integer> folderSubItemCount;
    protected ZXProject zxProject;


    {
        VBox.setVgrow(this, Priority.ALWAYS);
        getStyleClass().add("file-view");
    }

    public BaseFileView(FileManagerTab fileManagerTab) {
        this.folderSubItemCount = fileManagerTab.showCountProperty;
        zxProject = fileManagerTab.tabPane.zxProject;
        this.projectDirectoryPath = zxProject.projectPath;
        projectDirectoryPath.addListener((observable, oldValue, newValue) -> {
            refresh();
        });
    }

    public abstract void refresh();
}
