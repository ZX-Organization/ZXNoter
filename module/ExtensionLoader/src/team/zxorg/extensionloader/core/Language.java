package team.zxorg.extensionloader.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import team.zxorg.extensionloader.event.LanguageEventListener;
import team.zxorg.extensionloader.gson.GsonManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Language{
    private final String lang;

    public Language(String lang) {
        this.lang = lang;
    }

    public Language(Object lang) {
        this.lang = lang.toString();
    }

    @Override
    public String toString() {
        return getLang(lang);
    }

    private static final List<LanguageEventListener> eventListeners = new ArrayList<>();
    private static Locale locale = Locale.CHINA;
    /**
     * 缓存的消息格式化表
     */
    private static final HashMap<String, MessageFormat> messageFormatMap = new HashMap<>();
    /**
     * 全局语言表
     */
    private static final HashMap<String, String> global = new HashMap<>();
    /**
     * 被载入的全地区的语言表
     */
    private static final HashMap<String, List<LanguageInfo>> languageInfoMap = new HashMap<>();

    public static Map<String, MessageFormat> getLanguageMap() {
        return messageFormatMap;
    }


    /**
     * 设置全局语言
     *
     * @param key   语言键
     * @param value 语言值
     */
    public static void setGlobalLanguage(String key, String value) {
        global.put(key, value);
        reloadLanguages();
    }

    /**
     * 载入语言
     *
     * @param classLoader  类加载器
     * @param resourceName 语言资源名
     */
    public static void loadLanguage(ClassLoader classLoader, String resourceName) {
        LanguageInfo languageInfo = new LanguageInfo(classLoader.getResource(resourceName), GsonManager.parseJson(classLoader, resourceName));
        languageInfoMap.computeIfAbsent(languageInfo.getCode(), k -> new ArrayList<>()).add(languageInfo);
    }

    /**
     * 载入语言
     *
     * @param languageFilePath 语言文件地址
     */
    public static void loadLanguage(Path languageFilePath) {
        LanguageInfo languageInfo = null;
        try {
            languageInfo = new LanguageInfo(languageFilePath.toUri().toURL(), GsonManager.parseJson(languageFilePath));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        languageInfoMap.computeIfAbsent(languageInfo.getCode(), k -> new ArrayList<>()).add(languageInfo);
    }

    /**
     * 载入语言
     *
     * @param classLoader  类加载器
     * @param resourceName 语言资源名
     */
    public static void loadLanguage(ClassLoader classLoader, Path resourceName) {
        String name = resourceName.toString().replaceAll("\\\\", "/");
        LanguageInfo languageInfo = new LanguageInfo(classLoader.getResource(name), GsonManager.parseJson(classLoader, name));
        languageInfoMap.computeIfAbsent(languageInfo.getCode(), k -> new ArrayList<>()).add(languageInfo);
    }

    /**
     * 获取地区的语言代码
     *
     * @param locale 地区
     * @return 语言代码
     */
    private static String getCode(Locale locale) {
        return (locale.getLanguage() + "-" + locale.getCountry()).toLowerCase();
    }

    /**
     * 更改语言地区
     *
     * @param locale 语言地区
     */
    public static void setLocale(Locale locale) {
        Language.locale = locale;
        reloadLanguages();
    }

    private static final Pattern pattern = Pattern.compile("\\{\\{([^}]*)}}");
    /**
     * 当前的语言map 为了知道哪些语言条目被更新
     */
    private static HashMap<String, String> languageMap = new HashMap<>(0);

    /**
     * 重新为语言编制索引
     */
    public static void reloadLanguages() {
        //重新编制语言索引
        HashMap<String, String> newLanguageMap = new HashMap<>(global);

        List<LanguageInfo> languages = languageInfoMap.getOrDefault(getCode(locale), new ArrayList<>());
        for (LanguageInfo languageInfo : languages) {
            newLanguageMap.putAll(languageInfo.getLanguages());
        }

        //和前的语言map 进行比较 更新
        // 比较两个HashMap的差异
        Map<String, String> added = new HashMap<>(newLanguageMap);
        added.keySet().removeAll(languageMap.keySet());

        Map<String, String> removed = new HashMap<>(languageMap);
        removed.keySet().removeAll(newLanguageMap.keySet());

        Map<String, String> changed = new HashMap<>();
        Set<String> commonKeys = new HashSet<>(languageMap.keySet());
        commonKeys.retainAll(newLanguageMap.keySet());
        for (String key : commonKeys) {
            if (!languageMap.get(key).equals(newLanguageMap.get(key))) {
                changed.put(key, newLanguageMap.get(key));
            }
        }

        languageMap = newLanguageMap;
        MessageFormat mf;

        for (Map.Entry<String, String> entry : added.entrySet()) {
            mf = toMessageFormat(entry.getKey(), entry.getValue());
            for (LanguageEventListener listener : eventListeners)
                listener.onLanguageAdded(entry.getKey(), mf);
            messageFormatMap.put(entry.getKey(), mf);
        }
        for (Map.Entry<String, String> entry : removed.entrySet()) {
            mf = messageFormatMap.get(entry.getKey());
            for (LanguageEventListener listener : eventListeners)
                listener.onLanguageRemoved(entry.getKey(), mf);
            messageFormatMap.remove(entry.getKey());
        }
        for (Map.Entry<String, String> entry : changed.entrySet()) {
            mf = toMessageFormat(entry.getKey(), entry.getValue());
            messageFormatMap.put(entry.getKey(), mf);
            for (LanguageEventListener listener : eventListeners)
                listener.onLanguageChange(entry.getKey(), mf);
        }
    }

    /**
     * 语言转消息格式化
     *
     * @param key   语言键
     * @param value 语言值
     * @return 消息格式化
     */
    private static MessageFormat toMessageFormat(String key, String value) {
        Matcher matcher = pattern.matcher(value);
        StringBuilder resultBuffer = new StringBuilder();
        // 查找匹配并替换
        while (matcher.find()) {
            String match = matcher.group(1);
            String v = languageMap.get(match);

            //如果没找到引用则尝试在同级下找
            if (v == null) {
                v = languageMap.getOrDefault(key.substring(0, key.lastIndexOf(".") + 1) + match, match.replaceAll("[{}]", "*"));
            }
            matcher.appendReplacement(resultBuffer, v);
        }
        // 将剩余的部分追加到结果中
        matcher.appendTail(resultBuffer);
        return new MessageFormat(resultBuffer.toString());
    }

    /**
     * 语言转消息格式化
     *
     * @param value 语言值
     * @return 消息格式化
     */
    private static String getLang(String value) {
        Matcher matcher = pattern.matcher(value);
        StringBuilder resultBuffer = new StringBuilder();
        // 查找匹配并替换
        while (matcher.find()) {
            String match = matcher.group(1);
            String v = get(match);
            matcher.appendReplacement(resultBuffer, v);
        }
        // 将剩余的部分追加到结果中
        matcher.appendTail(resultBuffer);
        return resultBuffer.toString();
    }

    /**
     * 获取语言
     *
     * @param key  语言key
     * @param args 参数
     * @return 格式化后的语言
     */
    public static String get(String key, Object... args) {
        String v = getOrNull(key, args);
        if (v == null) {
            Logger.printStackTrace();
            v = Language.getOrNull(LanguageKey.MESSAGE_LANGUAGE_LOST, key);
            v = (v == null ? "LOST_LANGUAGE: " + key : v);
            Logger.warning(v);
            return v;
        }
        return v;
    }


    /**
     * 获取语言
     *
     * @param key  语言key
     * @param args 参数
     * @return 格式化后的语言
     */
    public static String get(Object key, Object... args) {
        return get(key.toString(), args);
    }

    /**
     * 获取语言
     *
     * @param key  语言key
     * @param args 参数
     * @return 格式化后的语言 没有则返回null
     */
    public static String getOrNull(String key, Object... args) {
        MessageFormat format = messageFormatMap.get(key);
        return (format != null) ? format.format(args) : null;
    }

    /**
     * 获取语言
     *
     * @param key  语言key
     * @param args 参数
     * @return 格式化后的语言 没有则返回null
     */
    public static String getOrNull(Object key, Object... args) {
        return getOrNull(key.toString(), args);
    }

    /**
     * 添加语言事件监听器
     *
     * @param listener 要添加的监听器
     */
    public static void addEventListener(LanguageEventListener listener) {
        eventListeners.add(listener);
    }

    /**
     * 移除语言事件监听器
     *
     * @param listener 被移除的监听器
     */
    public static void removeEventListener(LanguageEventListener listener) {
        eventListeners.remove(listener);
    }

    private static class LanguageInfo {
        private final URL from;
        private final String code;
        private final HashMap<String, String> languages;

        public String getCode() {
            return code;
        }

        public LanguageInfo(URL from, JsonElement languagesJson) {
            this.from = from;
            this.languages = new HashMap<>();
            traverseLanguageElement(languagesJson, null);
            code = languages.getOrDefault(LanguageKey.CONFIGURATION_LANGUAGE_CODE.toString(), "null").toLowerCase();
            if (code.equals("null"))
                Logger.warning(from + " '" + LanguageKey.CONFIGURATION_LANGUAGE_CODE + "' Language code is null");
        }

        public URL getFrom() {
            return from;
        }

        public HashMap<String, String> getLanguages() {
            return languages;
        }

        private void traverseLanguageElement(JsonElement jsonElement, String currentPath) {
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                for (String key : jsonObject.keySet()) {
                    String newPath = (currentPath == null ? "" : currentPath + ".") + key;
                    JsonElement childElement = jsonObject.get(key);
                    traverseLanguageElement(childElement, newPath);
                }
            } else if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                languages.put(currentPath, jsonPrimitive.getAsString());
            }
        }
    }

}
