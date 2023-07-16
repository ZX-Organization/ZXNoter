package team.zxorg.zxnoter.ui.main.stage.body.area.editor.setting.item;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.ui.component.ZXLabel;
import team.zxorg.zxnoter.ui.component.ZXVHBox;

public abstract class BaseSettingItem extends ZXVHBox {
    public ZXLabel title;
    private final Node content;


    public BaseSettingItem(String titleKey) {
        title = new ZXLabel(titleKey);
        title.setMinWidth(Region.USE_PREF_SIZE);
        content = initContent();
        if (content == null) {
            ZXLogger.warning("基本设置项内容为空。");
        }
        getChildren().addAll(title, content);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(6);
        HBox.setHgrow(content, Priority.ALWAYS);
    }

    /**
     * 初始化内容
     *
     * @return 内容容器
     */
    protected abstract Node initContent();
}
