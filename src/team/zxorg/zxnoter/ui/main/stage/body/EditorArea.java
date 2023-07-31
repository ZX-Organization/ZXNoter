package team.zxorg.zxnoter.ui.main.stage.body;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorLayout;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorTabPane;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.BaseEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.image.ImageEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.SettingEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.start.StartEditor;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;

import java.nio.file.Path;
import java.util.HashMap;

/**
 * 编辑区域
 */
public class EditorArea extends EditorLayout {
    public static BaseEditor dragTab;//拖拽中的Tab
    public static EditorTabPane dragTabPane;//拖拽中的TabPane
    public HashMap<String, BaseEditor> editorHashMap = new HashMap<>();
    public EditorTabPane editorTabPane;

    @Override
    public String toString() {
        return "(ROOT)" + super.toString() + "";
    }

    public EditorArea() {
        super(null);
        getStyleClass().add("editor-area");
        setOrientation(Orientation.HORIZONTAL);
        HBox.setHgrow(this, Priority.ALWAYS);

        editorTabPane = new EditorTabPane(this, this);
        getItems().add(editorTabPane);

        /*StartEditor startEditor = new StartEditor();
        editorTabPane.createEditor(startEditor);*/

        //setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        autoLayout();
    }

    public BaseEditor findEditor(Path openFile) {
        return findEditor(openFile, this);
    }


    private BaseEditor findEditor(Path openFile, EditorLayout editorLayout) {
        BaseEditor editor = null;
        for (Node node : editorLayout.getItems()) {
            if (node instanceof EditorLayout subEditorLayout) {
                editor = findEditor(openFile, subEditorLayout);
            } else if (node instanceof EditorTabPane editorTabPane) {
                for (Tab tab : editorTabPane.getTabs()) {
                    if (tab instanceof BaseEditor baseEditor) {

                    }
                }
            }
        }

        return editor;
    }


    public void openFile(FileItem openFile) {
        ZXLogger.info("打开文件 " + openFile);
        switch (openFile.fileType.type) {
            case image -> {
                if (editorTabPane.getParent()==null){
                    getItems().add(editorTabPane);
                }
                ImageEditor imageEditor = new ImageEditor(openFile);
                editorTabPane.createEditor(imageEditor);

            }
        }
    }
}
