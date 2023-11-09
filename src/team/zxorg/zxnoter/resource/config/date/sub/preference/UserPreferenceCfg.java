package team.zxorg.zxnoter.resource.config.date.sub.preference;

import team.zxorg.zxnoter.resource.config.date.sub.preference.editor.EditorCfg;
import team.zxorg.zxnoter.resource.config.date.sub.preference.res.ResourcesPackCfg;
import team.zxorg.zxnoter.resource.config.date.sub.preference.side.SideBarCfg;

/**
 * 用户偏好配置
 */
public class UserPreferenceCfg {
    /**
     * 语言代码 影响到全局的语言
     */
    public String languageCode;
    /**
     * 项目文件夹目录 默认在项目文件夹内创建新项目
     */
    public String projectsFolderPath;

    /**
     * 资源包配置
     */
    public ResourcesPackCfg resourcePacks;
    /**
     * 编辑器配置
     */
    public EditorCfg editor;
    /**
     * 侧边栏配置
     */
    public SideBarCfg sideBar;
}
