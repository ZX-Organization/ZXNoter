package team.zxorg.info;

import java.util.List;

/**
 * 扩展信息
 */
public class ExtensionInfo {
    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public Contact getContact() {
        return contact;
    }

    public List<String> getEntrypoints() {
        return entrypoints;
    }

    public Depends getDepends() {
        return depends;
    }

    /**
     * 扩展ID
     */
    public String id;
    /**
     * 版本
     */
    public String version;
    /**
     * 名称
     */
    public String name;
    /**
     * 描述
     */
    public String description;
    /**
     * 扩展图标
     */
    public String icon;
    /**
     * 开发者
     */
    public List<String> authors;
    /**
     * 联系方式
     */
    public Contact contact;
    /**
     * 入口点
     */
    public List<String> entrypoints;
    /**
     * 依赖
     */
    public Depends depends;

    /**
     * 依赖类
     */
    public static class Depends {
        /**
         * 扩展加载器
         */
        String extensionLoader;
        /**
         * 扩展API
         */
        String extensionApi;
        /**
         *  依赖的扩展列表
         */
        List<String> extensions;
    }

    /**
     * 联系方式类
     */
    public static class Contact {
        /**
         * 主页链接
         */
        public String homepage;
        /**
         * 源链接
         */
        public String source;
    }

    @Override
    public String toString() {
        return "ExtensionInfo{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", authors=" + authors +
                ", contact=" + contact +
                ", entrypoints=" + entrypoints +
                ", depends=" + depends +
                '}';
    }
}
