package team.zxorg.zxnoter.ui.main.component;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class BodyHBox extends HBox {

    public SideBar sideBar = new SideBar();//侧边栏
    public EditorArea editorArea = new EditorArea();//编辑器区域

    public BodyHBox() {
        getStyleClass().add("body");
        getChildren().addAll(sideBar, editorArea);
    }
}
