package team.zxorg.zxnoter.ui.editor;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import team.zxorg.zxnoter.map.ZXMap;

public abstract class BaseEditor extends HBox {
    public BaseEditor() {
        this.getStyleClass().add("editor");
    }
}
