
package team.zxorg.zxnoter.ui_old.component;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui_old.ComponentFactory;

import java.util.HashMap;

public class VToolGroupBar extends VBox {
    public VToolGroupBar() {
        setPrefWidth(26);
        setMinWidth(Region.USE_PREF_SIZE);
        getStyleClass().add("v-tool-group-bar");
    }

    HashMap<String, VBox> groups = new HashMap<>();

    public VBox getGroup(String groupName) {
        VBox aGroup = groups.get(groupName);
        if (aGroup == null) {
            aGroup = new VBox();
            aGroup.getStyleClass().add("group");
            groups.put(groupName, aGroup);
            getChildren().add(aGroup);
        }
        return aGroup;
    }

    public void addNode(String groupName, Node button) {
        getGroup(groupName).getChildren().add(button);
    }

    public Button addButton(String groupName, String key, String toolTip) {
        Button button = new Button();
        button.setShape(ZXResources.getSvg(key));
        button.setPrefSize(22, 22);
        button.getStyleClass().add("button");
        button.setFocusTraversable(false);
        VBox.setMargin(button, new Insets(2));
        if (toolTip != null) {
            button.setTooltip(ComponentFactory.getTooltip(toolTip));
        }
        addNode(groupName, button);
        return button;
    }


    public ToggleButton addToggleButton(String groupName, String key, String toolTip) {
        ToggleButton button = new ToggleButton();
        button.setShape(ZXResources.getSvg(key));
        button.setPrefSize(22, 22);
        button.getStyleClass().add("button");
        button.setFocusTraversable(false);
        VBox.setMargin(button, new Insets(2));
        if (toolTip != null) {
            button.setTooltip(ComponentFactory.getTooltip(toolTip));
        }
        addNode(groupName, button);
        return button;
    }
}
