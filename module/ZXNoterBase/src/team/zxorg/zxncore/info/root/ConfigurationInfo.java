package team.zxorg.zxncore.info.root;

import team.zxorg.zxncore.info.sub.preference.MiniProjectInfo;
import team.zxorg.zxncore.info.sub.LastTimeStatesInfo;
import team.zxorg.zxncore.info.sub.preference.UserPreferenceInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 全局的配置信息
 */
public class ConfigurationInfo {
    /**
     * 用户偏好
     */
    public UserPreferenceInfo userPreference;
    /**
     * 历史项目列表
     */
    public ArrayList<MiniProjectInfo> historicalProjects;
    /**
     * 上次打开的状态
     */
    public LastTimeStatesInfo lastTimeStates;
    /**
     * 扩展信息 方便插件存储数据
     */
    public HashMap<String, String> extension;
}
