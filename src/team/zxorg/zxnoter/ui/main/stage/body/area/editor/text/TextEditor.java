package team.zxorg.zxnoter.ui.main.stage.body.area.editor.text;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.base.BaseFileEditor;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;

import java.io.IOException;
import java.nio.file.Files;


public class TextEditor extends BaseFileEditor {
    TextArea textArea = new TextArea();

    public TextEditor(FileItem fileItem, ZXProject zxProject) {
        super(fileItem, zxProject);
        textArea.setFocusTraversable(false);
        textArea.getStyleClass().add("text-color-font-light");
        body.setCenter(textArea);
        reLoad();
    }

    private void reLoad(){
        try {
            textArea.setText(Files.readString(fileItem.path));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void saveFile() {

    }

    @Override
    protected void closed() {

    }
}
