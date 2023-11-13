package team.zxorg.zxncore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @param major  主要版本号 在发生不向后兼容的重大更改时递增
 * @param minor  次要版本号 在向后兼容的新功能添加时递增
 * @param patch  补丁版本号 在向后兼容的错误修复时递增
 * @param status 状态标识 软件的预发布状态
 */
public record ZXVersion(int major, int minor, int patch, ReleaseStatus status) {
    /**
     * 现在的版本号
     */
    public static final ZXVersion VERSION = new ZXVersion(0, 0, 0, ReleaseStatus.ALPHA);
    /**
     * 正则表达式模式，用于匹配版本
     * 例如：1.0.0-Alpha
     */
    public static final Pattern pattern = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(?:-(\\w+))?$");

    @Override
    public String toString() {
        return major + "." + minor + "." + patch + "-" + status.getDisplayName();
    }

    /**
     * 获取版本代号
     */
    public int getVersionCode() {
        return major * 100000 + minor * 1000 + patch * 10 + status().ordinal();
    }

    /**
     * 通过版本字符串解析成版本类
     *
     * @param version 版本字符串
     * @return 解析成功的类
     * @throws IllegalArgumentException 解析失败时抛出
     */
    public static ZXVersion resolve(String version) {
        Matcher matcher = pattern.matcher(version);
        if (matcher.matches()) {
            return new ZXVersion(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), ReleaseStatus.valueOf(matcher.group(4).toUpperCase()));
        } else {
            throw new IllegalArgumentException("Invalid version format");
        }
    }

    /**
     * 通过版本代号解析成版本类
     *
     * @param versionCode 版本代号
     * @return 解析成功的类
     */
    public static ZXVersion resolve(int versionCode) {
        return new ZXVersion(versionCode / 100000, versionCode / 1000 % 100, versionCode / 10 % 100, ReleaseStatus.values()[versionCode % 10]);
    }

    public enum ReleaseStatus {
        /**
         * 阿尔法 早期开发阶段
         */
        ALPHA("Alpha"),
        /**
         * 贝塔 相对稳定
         */
        BETA("Beta"),
        /**
         * 候选发布版 内部测试
         */
        RC("RC"),
        /**
         * 稳定版 稳定运行
         */
        STABLE("Stable");

        private final String displayName;

        ReleaseStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

}

