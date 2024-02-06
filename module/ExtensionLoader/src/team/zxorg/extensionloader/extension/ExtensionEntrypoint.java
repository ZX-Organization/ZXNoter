package team.zxorg.extensionloader.extension;

/**
 * 扩展入口点
 */
public interface ExtensionEntrypoint {
    /**
     * 扩展加载完成
     *
     * @param extension 此扩展对象
     * @param manager   扩展管理器
     */

    default void onLoaded(Extension extension, ExtensionManager manager) {

    }

    /**
     * 扩展初始化
     *
     * @param extension 此扩展对象
     * @param manager   扩展管理器
     */
    default void onInitialize(Extension extension, ExtensionManager manager) {

    }

    /**
     * 所有扩展初始化完成
     *
     * @param manager 扩展管理器
     */
    default void onAllInitialized(Extension extension, ExtensionManager manager) {

    }
}
