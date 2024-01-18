package team.zxorg.extension;

import team.zxorg.core.ZXVersion;

import java.util.List;
import java.util.Map;

/**
 * 扩展信息
 */
public class ExtensionInfo {

    /**
     * 扩展ID
     */
    String id;
    /**
     * 版本
     */
    ZXVersion version;
    /**
     * 名称
     */
    String name;
    /**
     * 描述
     */
    String description;
    /**
     * 扩展图标
     */
    String icon;
    /**
     * 开发者
     */
    List<String> authors;
    /**
     * 联系方式
     */

    Contact contact;
    /**
     * 入口点
     */

    List<String> entrypoints;
    /**
     * 依赖
     */

    Depends depends;

    /**
     * 扩展标签
     */
    List<String> tags;

    /**
     * 语言列表
     */
    List<String> languages;

    /**
     * 依赖类
     */
    public static class Depends {
        /**
         * 扩展API
         */
        ZXVersion extensionApi;
        /**
         * 依赖的扩展列表
         */
        Map<String, ZXVersion> extensions;
    }

    /**
     * 联系方式类
     */
    public static class Contact {
        /**
         * 主页链接
         */
        String homepage;
        /**
         * 源链接
         */
        String source;
    }


}
