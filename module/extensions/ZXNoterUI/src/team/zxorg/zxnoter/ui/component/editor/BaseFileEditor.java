package team.zxorg.zxnoter.ui.component.editor;

import javafx.application.Platform;
import team.zxorg.fxcl.component.flexview.FlexTab;
import team.zxorg.zxnoter.ui.ProjectView;

import java.nio.file.Path;

public abstract class BaseFileEditor extends FlexTab {
    ProjectView view;
    Path path;


    public BaseFileEditor(Path file, ProjectView view) {
        if (file == null || view == null)
            return;
        this.path = file;
        this.view = view;
        Platform.runLater(() -> open(path));
    }

    abstract public void create(Path path);

    abstract public void open(Path path);

    abstract public void save();

    abstract public void close();
}