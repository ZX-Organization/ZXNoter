package team.zxorg.zxnoter.uiframe.activitypane;

import team.zxorg.extensionloader.core.ConfigData;

import java.util.LinkedHashSet;

public class ActivityBarConfig extends ConfigData {


    double leftPaneWidth;
    double rightPaneWidth;
    double bottomPaneHeight;

    /**
     * 是否隐藏主栏
     */
    boolean hideMainBar;
    /**
     * 是否隐藏副栏
     */
    boolean hideSecondBar;
    /**
     * 主栏是否在左边
     */
    boolean mainBarIsLeft;
    /**
     * 主栏的项
     */
    LinkedHashSet<String> mainTopBarItems;
    /**
     * 主栏底部的项
     */
    LinkedHashSet<String> mainBottomBarItems;
    /**
     * 副栏的项
     */
    LinkedHashSet<String> secondBarItems;


    int activeMainBarTopItemIndex;
    int activeMainBarBottomItemIndex;
    int activeSecondBarItemIndex;

    public boolean containsItem(String item) {
        return mainTopBarItems.contains(item) || secondBarItems.contains(item) || mainBottomBarItems.contains(item);
    }

    public boolean removeItem(String item) {
        return mainTopBarItems.remove(item) || secondBarItems.remove(item) || mainBottomBarItems.remove(item);
    }
}
