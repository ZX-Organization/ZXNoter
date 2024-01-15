package team.zxorg.ui.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import team.zxorg.zxnoter.resource.GlobalResources;

public class ZXTextField extends TextField {
    public StringProperty promptTextKeyProperty = new SimpleStringProperty();

    {
        promptTextKeyProperty.addListener((observable, oldValue, newValue) -> promptTextProperty().bind(GlobalResources.getLanguageContent(newValue)));
    }


    /**
     * 设置提示文本key
     */
    public void setPromptTextKey(String promptTextKey) {
        this.promptTextKeyProperty.set(promptTextKey);
    }
}
