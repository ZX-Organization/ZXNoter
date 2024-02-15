package team.zxorg.zxnoter.uiframe.config;

import team.zxorg.extensionloader.core.ConfigData;

import java.util.Set;
import java.util.TreeSet;

public class ActivityBarConfig extends ConfigData {

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
    TreeSet<String> mainBarItems;
    /**
     * 主栏底部的项
     */
    TreeSet<String> bottomBarItems;
    /**
     * 副栏的项
     */
    TreeSet<String> secondBarItems;
}
