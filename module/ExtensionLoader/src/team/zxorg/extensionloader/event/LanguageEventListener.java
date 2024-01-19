package team.zxorg.extensionloader.event;

import java.text.MessageFormat;
import java.util.Locale;

public interface LanguageEventListener {
    /**
     * 语言改变事件
     *
     * @param key   变更的语言条目
     * @param language 变更后的语言值
     */
    default void onLanguageChange(String key, MessageFormat language) {

    }

    /**
     * 语言移除事件
     *
     * @param key   被移除的语言条目
     * @param language 被移除的语言值
     */
    default void onLanguageRemoved(String key, MessageFormat language) {

    }

    /**
     * 语言添加事件
     *
     * @param key      被添加的语言条目
     * @param language 被添加的语言
     */
    default void onLanguageAdded(String key, MessageFormat language) {

    }

    /**
     * 地区变更事件
     *
     * @param locale 变更的地区
     */
    default void onLocaleChange(Locale locale) {

    }

    /**
     * 语言重载事件
     */
    default void onReload() {

    }
}
