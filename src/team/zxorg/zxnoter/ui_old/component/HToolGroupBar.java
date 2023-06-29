package team.zxorg.zxnoter.ui_old.component;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui_old.ComponentFactory;

import java.util.HashMap;

public class HToolGroupBar extends HBox {
    public HToolGroupBar() {
        setPrefHeight(26);
        setMinHeight(Region.USE_PREF_SIZE);
        getStyleClass().add("h-tool-group-bar");
    }

    HashMap<String, HBox> groups = new HashMap<>();

    public HBox getGroup(String groupName) {
        HBox aGroup = groups.get(groupName);
        if (aGroup == null) {
            aGroup = new HBox();
            aGroup.getStyleClass().add("group");
            groups.put(groupName, aGroup);
            getChildren().add(aGroup);
        }
        return aGroup;
    }

    public void addNode(String groupName, Node button) {
        getGroup(groupName).getChildren().add(button);
        HBox.setMargin(button, new Insets(2));
    }

    public Pane addIcon(String groupName, String key) {
        Pane icon = ZXResources.getSvgPane(key);
        icon.setPrefSize(22,22);
        icon.setMaxSize(USE_PREF_SIZE,USE_PREF_SIZE);
        icon.getStyleClass().add("icon");
        addNode(groupName, icon);
        return icon;
    }

    public Button addButton(String groupName, String key, String toolTip) {
        Button button = new Button();
        button.setShape(ZXResources.getSvg(key));
        button.setPrefSize(22, 22);
        button.setFocusTraversable(false);
        //button.getStyleClass().add("button");
        //HBox.setMargin(button, new Insets(2));
        if (toolTip != null) {
            button.setTooltip(ComponentFactory.getTooltip(toolTip));
        }
        addNode(groupName, button);
        return button;
    }

    public void addControl(String groupName, Control button, String toolTip) {
        getGroup(groupName).getChildren().add(button);
        HBox.setMargin(button, new Insets(2));
        if (toolTip != null) {
            button.setTooltip(ComponentFactory.getTooltip(toolTip));
        }
    }


    public ToggleButton addToggleButton(String groupName, String key, String toolTip) {
        ToggleButton button = new ToggleButton();
        button.setShape(ZXResources.getSvg(key));
        button.setPrefSize(22, 22);
        button.getStyleClass().add("button");
        button.setFocusTraversable(false);
       // HBox.setMargin(button, new Insets(2));
        if (toolTip != null) {
            button.setTooltip(ComponentFactory.getTooltip(toolTip));
        }
        addNode(groupName, button);
        return button;
    }
}
