package team.zxorg.zxnoter.resource.config.date;

import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.resource.config.date.sub.MiniProjectCfg;
import team.zxorg.zxnoter.resource.config.date.sub.LastTimeStatesCfg;
import team.zxorg.zxnoter.resource.config.date.sub.preference.UserPreferenceCfg;

import java.util.ArrayList;

/**
 * 全局的配置
 */
public class Configuration {
    /**
     * 用户偏好
     */
    public UserPreferenceCfg userPreference;
    /**
     * 历史项目列表
     */
    public ArrayList<MiniProjectCfg> historicalProjects;
    /**
     * 上次打开的状态
     */
    public LastTimeStatesCfg lastTimeStates;
    /**
     * 扩展信息 方便插件存储数据
     */
    public JSONObject extension;
}
