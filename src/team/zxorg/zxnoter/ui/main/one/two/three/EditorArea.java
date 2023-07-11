package team.zxorg.zxnoter.ui.main.one.two.three;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.zxnoter.ui.main.one.two.three.four.EditorLayout;
import team.zxorg.zxnoter.ui.main.one.two.three.four.EditorTabPane;
import team.zxorg.zxnoter.ui.main.one.two.three.four.five.BaseEditor;
import team.zxorg.zxnoter.ui.main.one.two.three.four.five.SettingEditor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 编辑区域
 */
public class EditorArea extends EditorLayout {
    public static BaseEditor dragTab;//拖拽中的Tab
    public static EditorTabPane dragTabPane;//拖拽中的TabPane
    public HashMap<String, BaseEditor> editorHashMap = new HashMap<>();

    @Override
    public String toString() {
        return "(ROOT)" + super.toString() + "";
    }

    public EditorArea() {
        super(null);
        getStyleClass().add("editor-area");
        setOrientation(Orientation.HORIZONTAL);
        HBox.setHgrow(this, Priority.ALWAYS);

        EditorTabPane editorTabPane = new EditorTabPane(this, this);
        getItems().add(editorTabPane);


        for (int i = 0; i < 6; i++) {
            SettingEditor settingEditor = new SettingEditor(this);
            settingEditor.setText("    " + i + "    ");
            editorTabPane.createEditor(settingEditor);
        }
        autoLayout();
    }

}
