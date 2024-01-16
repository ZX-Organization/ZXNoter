package team.zxorg.api;

import team.zxorg.extension.ExtensionManager;

/**
 * 扩展入口点
 */
public interface ExtensionEntrypoint {
    /**
     * 扩展初始化
     */
    default void onInitialize() {

    }

    /**
     * 扩展被启用
     */
    default void onEnable() {

    }

    /**
     * 扩展被禁用
     */
    default void onDisable() {

    }

    /**
     * 设置扩展管理器
     *
     * @param manager 扩展管理器
     */
    default void setExtensionManager(ExtensionManager manager) {

    }
}
