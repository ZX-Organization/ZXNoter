package team.zxorg.zxnoter.api.extension;

import team.zxorg.zxnoter.api.core.IEventListener;
import team.zxorg.zxnoter.api.version.IVersion;

import java.util.List;

public interface IExtension {

    /**
     * 获取扩展id
     *
     * @return 扩展id
     */
    String getId();

    /**
     * 获取扩展版本
     *
     * @return 扩展版本
     */
    IVersion getVersion();

    /**
     * 获取依赖列表
     *
     * @return 依赖的扩展列表
     */
    List<IExtension> getDependencies();

    /**
     * 获取日志记录器
     *
     * @return 扩展日志记录器
     */
    IExtensionLogger getLogger();


    /**
     * 获取应用
     *
     * @return 应用对象
     */
    IApp getApp();

    /**
     * 注册事件监听器
     *
     * @param eventType 事件类型
     * @param listener  监听器
     */
    void registerEventListener(String eventType, IEventListener listener);

    /**
     * 触发事件
     *
     * @param eventType 事件类型
     * @param eventData 事件数据
     */
    void triggerEvent(String eventType, Object eventData);

    /**
     * 获取扩展名称
     *
     * @return 扩展名称
     */
    String getName();


    /**
     * 被载入
     */
    default void onLoad() {
    }


    /**
     * 被启用
     */
    default void onEnable() {
    }

    /**
     * 被禁用
     */
    default void onDisable() {
    }

    /**
     * 被卸载
     */
    default void onUnload() {
    }

}
