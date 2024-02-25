package team.zxorg.zxnoter.ui.component.titlebar.menu;

/**
 * 菜单项信息
 */
public class MenuItemInfo {
    /**
     * 菜单项索引   根据索引排序 作用于菜单项分组
     */
    private int index;
    /**
     * 菜单项所属组
     */
    private String group;
    /**
     * 菜单项id
     */
    private String id;
    /**
     * 菜单项图标key
     */
    private String icon;
    /**
     * 行为class路径 被选中后调用
     */
    private String c;
}
