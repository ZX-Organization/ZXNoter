package team.zxorg.extensionloader.extension;

import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionManager;

/**
 * 扩展入口点
 */
public interface ExtensionEntrypoint {
    /**
     * 扩展初始化
     */
    default void onInitialize(Extension extension, ExtensionManager manager) {

    }
}
