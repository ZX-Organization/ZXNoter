package team.zxorg.core;

import team.zxorg.gson.ZXGson;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZXLanguage {
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

    /**
     * 设置全局语言
     *
     * @param key   语言键
     * @param value 语言值
     */
    public static void setGlobalLanguage(String key, String value) {
        global.put(key, value);
        flushLanguages();
    }

    /**
     * 载入语言
     *
     * @param classLoader  类加载器
     * @param resourceName 语言资源名
     */
    public static void loadLanguage(ClassLoader classLoader, String resourceName) {
        LanguageInfo languageInfo = new LanguageInfo(classLoader.getResource(resourceName), ZXGson.parseJson(classLoader, resourceName));
        languageInfoMap.computeIfAbsent(languageInfo.getCode(), k -> new ArrayList<>()).add(languageInfo);
    }

    /**
     * 获取地区的语言代码
     *
     * @param locale 地区
     * @return 语言代码
     */
    private static String getCode(Locale locale) {
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    /**
     * 更改语言地区
     *
     * @param locale 语言地区
     */
    public static void setLocale(Locale locale) {
        ZXLanguage.locale = locale;
        flushLanguages();
    }

    private static final Pattern pattern = Pattern.compile("\\{\\{([^}]*)}}");

    /**
     * 重新为语言编制索引
     */
    private static void flushLanguages() {
        //重新编制语言索引
        HashMap<String, String> languageMap = new HashMap<>(global);

        List<LanguageInfo> languages = languageInfoMap.getOrDefault(getCode(locale), new ArrayList<>());
        for (LanguageInfo languageInfo : languages) {
            languageMap.putAll(languageInfo.getLanguages());
        }

        //重新索引全部格式化消息
        messageFormatMap.clear();
        for (Map.Entry<String, String> entry : languageMap.entrySet()) {
            Matcher matcher = pattern.matcher(entry.getValue());
            if (matcher.hasMatch()) {
                //节省开销
                messageFormatMap.put(entry.getKey(), new MessageFormat(entry.getValue()));
                continue;
            }
            StringBuilder resultBuffer = new StringBuilder();
            // 查找匹配并替换
            while (matcher.find()) {
                String match = matcher.group(1);
                String v = languageMap.get(match);

                //如果没找到引用则尝试在同级下找
                if (v == null) {
                    String key = entry.getKey();
                    v = languageMap.getOrDefault(key.substring(0, key.lastIndexOf(".") + 1) + match, match.replaceAll("[{}]", "*"));
                }
                matcher.appendReplacement(resultBuffer, v);
            }
            // 将剩余的部分追加到结果中
            matcher.appendTail(resultBuffer);
            messageFormatMap.put(entry.getKey(), new MessageFormat(resultBuffer.toString()));
        }

        languageMap.clear();
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
            ZXLogger.printStackTrace();
            v = ZXLanguage.getOrNull("message.language.lost", key);
            v = (v == null ? "LOST_LANGUAGE: " + key : v);
            ZXLogger.warning(v);
            return v;
        }
        return v;
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
}
