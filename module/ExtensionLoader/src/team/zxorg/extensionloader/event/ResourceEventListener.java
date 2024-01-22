package team.zxorg.extensionloader.event;

public interface ResourceEventListener {
    // 懒得写了 基本是用不着 :)
    /**
     * 语言改变事件
     *
     * @param key  变更的相对路径
     * @param file 变更后的文件路径
     *//*
    default void onResourceChange(Path key, Path file) {

    }

    *//**
     * 资源包移除事件
     *
     * @param resourcePack 被移除的资源包
     *//*
    default void onPackRemoved(ResourcePack resourcePack) {

    }

    *//**
     * 资源包添加事件
     *
     * @param resourcePack 被添加的资源包
     *//*
    default void onPackAdded(ResourcePack resourcePack) {

    }

    *//**
     * 资源包列表被修改
     *//*
    default void onEnablePacksChange(ResourcePack resourcePack) {

    }*/

    /**
     * 资源重载事件
     */
    default void onReload() {

    }
}
