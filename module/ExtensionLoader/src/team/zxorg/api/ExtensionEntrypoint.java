package team.zxorg.api;

import team.zxorg.extension.Extension;
import team.zxorg.extension.ExtensionManager;

/**
 * 扩展入口点
 */
public interface ExtensionEntrypoint {
    /**
     * 扩展初始化
     */
    default void onInitialize(Extension extension, ExtensionManager manager) {

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


}
