package team.zxorg.zxnoter.ui.main.stage.body;

import javafx.geometry.Orientation;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorLayout;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorTabPane;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.BaseEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.SettingEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.start.StartEditor;

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

        StartEditor startEditor = new StartEditor();
        editorTabPane.createEditor(startEditor);

        SettingEditor settingEditor = new SettingEditor();
        editorTabPane.createEditor(settingEditor);

        //setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        autoLayout();
    }

}
