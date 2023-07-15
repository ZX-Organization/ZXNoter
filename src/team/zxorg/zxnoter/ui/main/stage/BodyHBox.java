package team.zxorg.zxnoter.ui.main.stage;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.ui.main.stage.body.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.body.SideBar;

public class BodyHBox extends HBox {

    public SideBar sideBar = new SideBar();//侧边栏
    public EditorArea editorArea = new EditorArea();//编辑器区域

    public BodyHBox() {
        getStyleClass().add("body");
        getChildren().addAll(sideBar, editorArea);
        VBox.setVgrow(this, Priority.ALWAYS);
    }
}
