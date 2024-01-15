package team.zxorg.fxcl.javafx.sub.editor.flexeditor;

import javafx.scene.control.Tab;

import java.nio.file.Path;

/**
 * 灵活编辑器 选项卡
 */
public abstract class FlexTab extends Tab {

    protected final Path path;

    public FlexTab(Path path) {
        this.path = path;

        //监听所属选项卡窗格 重新注册事件
        /*tabPaneProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue.lookup("#" + getId()));
        });*/


    }

    public FlexArea getArea() {
        return getFlexTabPane().getArea();
    }

    public FlexTabPane getFlexTabPane() {
        return (FlexTabPane)getTabPane();
    }
    /**
     * 关闭此选项卡
     */
    public void close() {

    }





}
