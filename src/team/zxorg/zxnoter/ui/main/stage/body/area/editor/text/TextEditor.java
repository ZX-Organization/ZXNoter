package team.zxorg.zxnoter.ui.main.stage.body.area.editor.text;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.component.ZXLabel;
import team.zxorg.zxnoter.ui.component.ZXStatus;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.base.BaseFileEditor;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;


public class TextEditor extends BaseFileEditor {
    TextArea textArea = new TextArea();
    Charset charset = Charset.forName("UTF-8");

    Label charsetLabel = new Label(charset.name());
    ZXStatus charsetStatus = new ZXStatus(charsetLabel);

    ObjectProperty<Integer> rowH = new SimpleObjectProperty<>(0);
    ObjectProperty<Integer> rowV = new SimpleObjectProperty<>(0);
    ZXStatus cursorPositionStatus = new ZXStatus(new ZXLabel("editor.text-editor.status.cursor", rowH, rowV));

    public TextEditor(FileItem fileItem, ZXProject zxProject) {
        super(fileItem, zxProject);

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
            textArea.setText(Files.readString(fileItem.path, charset));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void saveFile() {
        try {
            Files.writeString(fileItem.path, textArea.getText(), charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void closed() {

    }
}
