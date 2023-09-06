package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.pane.preference;

import javafx.geometry.Insets;
import javafx.scene.text.Font;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.ZXLabel;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item.BaseSettingItem;

import java.util.ArrayList;

public class BaseSettingPane {

    /**
     * 设置标题
     */
    public ZXLabel title;

    /**
     * 包含的设置项
     */
    public ArrayList<BaseSettingItem> settingItems = new ArrayList<>();
    /**
     * 对应的设置容器物品
     */
    public SettingPaneItem settingPaneItem;


    public BaseSettingPane(String languageKey) {
        title = new ZXLabel(languageKey, ZXColor.FONT_LIGHT);
        title.setFont(Font.font(18));
        title.setPadding(new Insets(6, 0, 0, 0));
        settingPaneItem = new SettingPaneItem(this);
    }

    public void addSettingPaneItem(BaseSettingPane settingPane) {
        settingPaneItem.thisTreeItem.getChildren().add(new SettingPaneItem(settingPane).thisTreeItem);
    }

}
