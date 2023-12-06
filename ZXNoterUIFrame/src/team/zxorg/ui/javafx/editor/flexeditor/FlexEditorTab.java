package team.zxorg.ui.javafx.editor.flexeditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.nio.file.Path;
import java.util.UUID;

/**
 * 灵活编辑器 选项卡
 */
public abstract class FlexEditorTab extends Tab {
    protected final FlexEditorArea editorArea;
    protected final Path path;

    public FlexEditorTab(FlexEditorArea editorArea, Path path) {
        this.editorArea = editorArea;
        this.path = path;
        setId(UUID.randomUUID().toString());

        //监听所属选项卡窗格 重新注册事件
        /*tabPaneProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue.lookup("#" + getId()));
        });*/

    }




    /**
     * 关闭此选项卡
     */
    public void close() {

    }

}
