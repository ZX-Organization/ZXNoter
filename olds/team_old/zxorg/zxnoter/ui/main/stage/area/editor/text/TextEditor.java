package team.zxorg.zxnoter.ui.main.stage.area.editor.text;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import team.zxorg.zxnoter.ui.component.ZXLabel;
import team.zxorg.zxnoter.ui.component.ZXStatus;
import team.zxorg.zxnoter.ui.main.stage.area.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.area.editor.base.BaseEditor;
import team.zxorg.zxnoter.ui.main.stage.side.filemanager.FileItem;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;


public class TextEditor extends BaseEditor {
    TextArea textArea = new TextArea();
    Charset charset = Charset.forName("UTF-8");

    Label charsetLabel = new Label(charset.name());
    ZXStatus charsetStatus = new ZXStatus(charsetLabel);

    ObjectProperty<Integer> rowH = new SimpleObjectProperty<>(0);
    ObjectProperty<Integer> rowV = new SimpleObjectProperty<>(0);
    ZXStatus cursorPositionStatus = new ZXStatus(new ZXLabel("editor.text-editor.status.cursor", rowH, rowV));

    public TextEditor(Path path, EditorArea editorArea) {
        super(path, editorArea);

        zxStatuses.add(cursorPositionStatus);//添加状态
        zxStatuses.add(charsetStatus);//添加状态

        textArea.setFocusTraversable(false);
        textArea.getStyleClass().add("text-color-font-light");
        body.setCenter(textArea);

        textArea.setOnKeyPressed((event) -> {
            isEdited.set(true);
        });
        textArea.setOnMouseClicked(event -> {
            String[] strs = (textArea.getText() + " ").substring(0, textArea.getSelection().getStart() + 1).split("\n");
            rowH.set(strs.length);
            rowV.set(strs[strs.length - 1].length());
        });


        reLoad();
    }

    private void reLoad() {
        try {
            textArea.setText(Files.readString(path, charset));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void saveFile() {
        try {
            Files.writeString(path, textArea.getText(), charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void closed() {

    }
}
