package team.zxorg.fxcl.component.flexview;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;

import java.nio.file.Path;

/**
 * 灵活编辑器 选项卡
 */
public abstract class FlexTab extends Tab {



    public FlexTab() {

        //监听所属选项卡窗格 重新注册事件
        /*tabPaneProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue.lookup("#" + getId()));
        });*/
        setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                event.consume();
            }
        });

    }

    public FlexArea getArea() {
        return getFlexTabPane().getArea();
    }

    public FlexTabPane getFlexTabPane() {
        return (FlexTabPane)getTabPane();
    }





}
