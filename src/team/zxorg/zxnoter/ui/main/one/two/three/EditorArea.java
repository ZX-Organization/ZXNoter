package team.zxorg.zxnoter.ui.main.one.two.three;

import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.zxnoter.ui.main.one.two.three.four.EditorLayout;
import team.zxorg.zxnoter.ui.main.one.two.three.four.EditorTabPane;
import team.zxorg.zxnoter.ui.main.one.two.three.four.five.BaseEditor;
import team.zxorg.zxnoter.ui.main.one.two.three.four.five.SettingEditor;

import java.util.HashMap;

/**
 * 编辑区域
 */
public class EditorArea extends EditorLayout {
    public BaseEditor dragTab;//拖拽中的Tab
    public EditorTabPane dragTabPane;//拖拽中的TabPane
    public HashMap<String, BaseEditor> editorHashMap = new HashMap<>();

    public EditorArea() {

        getStyleClass().add("editor-area");
        setOrientation(Orientation.HORIZONTAL);
        HBox.setHgrow(this, Priority.ALWAYS);

        {
            EditorTabPane editorTabPane = new EditorTabPane(this, this);
            getItems().add(editorTabPane);

            {
                BaseEditor baseEditor = editorTabPane.createEditor(SettingEditor.class);
                baseEditor.setText("1111");
            }
            {
                BaseEditor baseEditor = editorTabPane.createEditor(SettingEditor.class);
                baseEditor.setText("2222");
            }
            {
                BaseEditor baseEditor = editorTabPane.createEditor(SettingEditor.class);
                baseEditor.setText("555");
            }
        }
        {
            EditorTabPane editorTabPane = new EditorTabPane(this, this);

            getItems().add(editorTabPane);
            {
                BaseEditor baseEditor = editorTabPane.createEditor(SettingEditor.class);
                baseEditor.setText("3333");
            }
            {
                BaseEditor baseEditor = editorTabPane.createEditor(SettingEditor.class);
                baseEditor.setText("4444");
            }

        }


        //setDividerPositions(0.5, 0.5);

    }

}
