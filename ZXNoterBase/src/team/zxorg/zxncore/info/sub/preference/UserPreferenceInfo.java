package team.zxorg.zxncore.info.sub.preference;

import team.zxorg.zxncore.info.sub.preference.editor.EditorInfo;
import team.zxorg.zxncore.info.sub.preference.res.ResourcesPackInfo;
import team.zxorg.zxncore.info.sub.preference.side.SideBarInfo;

/**
 * 用户偏好配置
 */
public class UserPreferenceInfo {
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
    public ResourcesPackInfo resourcePacks;
    /**
     * 编辑器配置
     */
    public EditorInfo editor;
    /**
     * 侧边栏配置
     */
    public SideBarInfo sideBar;
}
