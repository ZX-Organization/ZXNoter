package team.zxorg.mapedit;

import team.zxorg.zxnoter.ui.ProjectView;
import team.zxorg.zxnoter.ui.component.editor.BaseFileEditor;

import java.nio.file.Path;

public class MusicMapFileEditor extends BaseFileEditor {


    public MusicMapFileEditor(Path file, ProjectView view) {
        super(file, view);
    }

    @Override
    public void open(Path path) {
        setText(path.getFileName().toString());
    }

    @Override
    public void save() {

    }

    @Override
    public void close() {

    }
}
