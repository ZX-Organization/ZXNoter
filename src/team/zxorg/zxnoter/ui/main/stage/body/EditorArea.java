package team.zxorg.zxnoter.ui.main.stage.body;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.project.ZXProject;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorLayout;
import team.zxorg.zxnoter.ui.main.stage.body.area.EditorTabPane;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.base.BaseFileEditor;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.base.BaseTab;
import team.zxorg.zxnoter.ui.main.stage.body.side.filemanager.FileItem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * 编辑区域
 */
public class EditorArea extends EditorLayout {
    public static BaseTab dragTab;//拖拽中的Tab
    public static EditorTabPane dragTabPane;//拖拽中的TabPane
    public HashMap<String, BaseTab> editorHashMap = new HashMap<>();
    public EditorTabPane rootEditorTabPane;
    public ObjectProperty<EditorTabPane> focusEditorTabPane = new SimpleObjectProperty<>();
    public ObjectProperty<BaseTab> focusTab = new SimpleObjectProperty<>();


    @Override
    public String toString() {
        return "(ROOT)" + super.toString() + "";
    }

    public EditorArea(ZXProject zxProject) {
        super(null, zxProject);
        getStyleClass().add("editor-area");
        setOrientation(Orientation.HORIZONTAL);
        HBox.setHgrow(this, Priority.ALWAYS);

        rootEditorTabPane = new EditorTabPane(this, this, zxProject);
        focusEditorTabPane.addListener((observable, oldValue, newValue) -> {
            newValue.getStyleClass().add("tab-pane-focused");
            if (oldValue != null) {
                oldValue.getStyleClass().remove("tab-pane-focused");
                oldValue.getSelectionModel().selectedItemProperty().removeListener(searchTabChangeListener);
            }
            newValue.getSelectionModel().selectedItemProperty().addListener(searchTabChangeListener);
            focusTab.set((BaseTab)  newValue.getSelectionModel().getSelectedItem());
            System.out.println("焦点变更:" + newValue + newValue.getStyleClass());
        });
        focusEditorTabPane.set(rootEditorTabPane);
        getItems().add(rootEditorTabPane);

        /*StartEditor startEditor = new StartEditor();
        editorTabPane.createEditor(startEditor);*/

        //setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        autoLayout();
    }

     ChangeListener<Tab> searchTabChangeListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
            focusTab.set((BaseTab) newValue);
        }
    };

    public BaseTab findEditor(Path openFile) {
        return findEditor(openFile, this);
    }


    private BaseTab findEditor(Path openFile, EditorLayout editorLayout) {
        BaseTab editor = null;
        for (Node node : editorLayout.getItems()) {
            if (node instanceof EditorLayout subEditorLayout) {
                editor = findEditor(openFile, subEditorLayout);
            } else if (node instanceof EditorTabPane editorTabPane) {
                for (Tab tab : editorTabPane.getTabs()) {
                    if (tab instanceof BaseTab baseTab) {

                    }
                }
            }
        }

        return editor;
    }


    public void openFile(FileItem openFile) {
        ZXLogger.info("打开文件 " + openFile);

        //检查是否已被打开
        BaseFileEditor fileEditor = zxProject.fileEditorMap.get(openFile.path);
        if (fileEditor != null) {
            if (fileEditor.getTabPane() == null) {
                zxProject.fileEditorMap.remove(fileEditor.getFileItem().path);
                return;
            }

            //获得焦点
            fileEditor.getTabPane().getSelectionModel().select(fileEditor);
            return;
        }

        //如果文件类型拥有编辑器则构建
        Class fileEditorClass = openFile.fileType.editor;
        if (fileEditorClass != null) {


            BaseFileEditor editor;
            try {
                Constructor<BaseFileEditor> constructor = fileEditorClass.getDeclaredConstructor(FileItem.class, ZXProject.class);
                editor = constructor.newInstance(openFile, zxProject);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            zxProject.fileEditorMap.put(openFile.path, editor);
            focusEditorTabPane.get().createEditor(editor);

        }
        /*switch (openFile.fileType.type) {
            case image -> {
                if (editorTabPane.getParent() == null) {
                    getItems().add(editorTabPane);
                }
                ImageViewEditor imageViewEditor = new ImageViewEditor(openFile, zxProject);
                editorTabPane.createEditor(imageViewEditor);

            }
        }*/
    }
}
