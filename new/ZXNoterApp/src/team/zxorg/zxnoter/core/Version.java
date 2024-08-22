package team.zxorg.zxnoter.core;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版本类，用于解析、比较和表示软件版本号。
 * 支持标准的主版本号.次版本号.修订号-发布状态格式，如 "1.2.3-beta"。
 * 还支持版本比较符号，如 ">1.2.3" 或 "<=2.0.0"。
 */
public class Version implements Comparable<Version> {
    private static final Pattern VERSION_PATTERN = Pattern.compile("^((?:>=?|<=?|=|~)?)\s*(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?(?:-(\\w+))?$");

    private final int major;
    private final int minor;
    private final int patch;
    private final ReleaseStatus status;
    private final VersionCompare compare;

    /**
     * 通过版本字符串构造 Version 对象。
     *
     * @param versionString 版本字符串，如 "1.2.3-beta" 或 ">=2.0.0"
     * @throws IllegalArgumentException 如果版本字符串格式无效
     */
    public Version(String versionString) {
        Matcher matcher = VERSION_PATTERN.matcher(versionString.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("无效的版本字符串: " + versionString);
        }

        String compareSign = matcher.group(1);
        this.compare = compareSign.isEmpty() ? VersionCompare.EQUAL : VersionCompare.fromSign(compareSign);

        this.major = Integer.parseInt(matcher.group(2));
        this.minor = parseVersionPart(matcher.group(3));
        this.patch = parseVersionPart(matcher.group(4));
        this.status = ReleaseStatus.fromString(matcher.group(5));
    }

    /**
     * 通过版本号各部分构造 Version 对象。
     *
     * @param major 主版本号
     * @param minor 次版本号
     * @param patch 修订号
     * @param status 发布状态
     */
    public Version(int major, int minor, int patch, ReleaseStatus status) {
        this(major, minor, patch, status, VersionCompare.EQUAL);
    }

    /**
     * 通过版本号各部分和比较符构造 Version 对象。
     *
     * @param major 主版本号
     * @param minor 次版本号
     * @param patch 修订号
     * @param status 发布状态
     * @param compare 版本比较符
     */
    public Version(int major, int minor, int patch, ReleaseStatus status, VersionCompare compare) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.status = Objects.requireNonNull(status);
        this.compare = Objects.requireNonNull(compare);
    }

    /**
     * 从整数形式的版本代码解析出 Version 对象。
     *
     * @param versionCode 整数形式的版本代码
     * @return 解析出的 Version 对象
     */
    public static Version resolve(int versionCode) {
        int major = versionCode / 1000000;
        int minor = (versionCode / 10000) % 100;
        int patch = (versionCode / 100) % 100;
        int statusOrdinal = versionCode % 100;

        if (statusOrdinal >= ReleaseStatus.values().length) {
            throw new IllegalArgumentException("无效的版本代码: " + versionCode);
        }

        return new Version(major, minor, patch, ReleaseStatus.values()[statusOrdinal]);
    }

    private static int parseVersionPart(String part) {
        return part == null ? 0 : Integer.parseInt(part);
    }

    /**
     * 检查给定的版本是否满足当前版本的要求。
     *
     * @param version 要检查的版本
     * @return 如果给定版本满足要求则返回 true，否则返回 false
     */
    public boolean isSupported(Version version) {
        return compare.compare(version, this);
    }

    /**
     * 返回版本的字符串表示。
     *
     * @return 版本的字符串表示，如 ">=1.2.3-beta"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(compare.getSign())
                .append(major).append('.').append(minor).append('.').append(patch);
        if (status != ReleaseStatus.EMPTY) {
            sb.append('-').append(status.getSign());
        }
        return sb.toString();
    }

    /**
     * 获取版本的整数表示。
     *
     * @return 版本的整数表示
     */
    public int getCode() {
        return major * 100000 + minor * 1000 + patch * 10 + status.ordinal();
    }

    /**
     * 比较两个版本的大小。
     *
     * @param other 要比较的其他版本
     * @return 如果当前版本大于其他版本返回正数，相等返回0，小于返回负数
     */
    @Override
    public int compareTo(Version other) {
        int result = Integer.compare(this.major, other.major);
        if (result == 0) {
            result = Integer.compare(this.minor, other.minor);
            if (result == 0) {
                result = Integer.compare(this.patch, other.patch);
                if (result == 0) {
                    // 确保 EMPTY（正式版本）大于其他所有状态
                    if (this.status == ReleaseStatus.EMPTY && other.status != ReleaseStatus.EMPTY) {
                        return 1;
                    } else if (this.status != ReleaseStatus.EMPTY && other.status == ReleaseStatus.EMPTY) {
                        return -1;
                    }
                    result = this.status.compareTo(other.status);
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version)) return false;
        Version version = (Version) o;
        return major == version.major &&
                minor == version.minor &&
                patch == version.patch &&
                status == version.status &&
                compare == version.compare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, status, compare);
    }


    /**
     * 版本的发布状态枚举。
     */
    public enum ReleaseStatus {
        ALPHA("alpha"),
        BETA("beta"),
        RC("rc"),
        EMPTY(""),
        STABLE("stable");

        private final String sign;

        ReleaseStatus(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }

        /**
         * 从字符串解析发布状态。
         *
         * @param str 表示发布状态的字符串
         * @return 对应的 ReleaseStatus 枚举值
         */
        public static ReleaseStatus fromString(String str) {
            if (str == null || str.isEmpty()) {
                return EMPTY;
            }
            return valueOf(str.toUpperCase());
        }
    }

    /**
     * 版本比较符枚举。
     */
    public enum VersionCompare {
        GREATER_THAN(">") {
            @Override
            public boolean compare(Version v1, Version v2) {
                return v1.compareTo(v2) > 0;
            }
        },
        GREATER_THAN_EQUAL(">=") {
            @Override
            public boolean compare(Version v1, Version v2) {
                return v1.compareTo(v2) >= 0;
            }
        },
        EQUAL("~") {
            @Override
            public boolean compare(Version v1, Version v2) {
                return v1.equals(v2);
            }
        },
        LESS_THAN_EQUAL("<=") {
            @Override
            public boolean compare(Version v1, Version v2) {
                return v1.compareTo(v2) <= 0;
            }
        },
        LESS_THAN("<") {
            @Override
            public boolean compare(Version v1, Version v2) {
                return v1.compareTo(v2) < 0;
            }
        };

        private final String sign;

        VersionCompare(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }

        /**
         * 比较两个版本。
         *
         * @param v1 第一个版本
         * @param v2 第二个版本
         * @return 如果比较结果满足当前比较符的条件则返回 true，否则返回 false
         */
        public abstract boolean compare(Version v1, Version v2);

        /**
         * 从比较符号解析 VersionCompare 枚举值。
         *
         * @param sign 比较符号
         * @return 对应的 VersionCompare 枚举值
         * @throws IllegalArgumentException 如果给定的比较符号无效
         */
        public static VersionCompare fromSign(String sign) {
            for (VersionCompare compare : values()) {
                if (compare.sign.equals(sign)) {
                    return compare;
                }
            }
            throw new IllegalArgumentException("无效的比较符号: " + sign);
        }
    }
}