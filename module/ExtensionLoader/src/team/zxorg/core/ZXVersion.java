package team.zxorg.core;

public class ZXVersion {

    private final int major;
    private final int minor;
    private final int patch;
    private final ReleaseStatus status;
    private final VersionCompare compare;

    public ZXVersion(String versionString) {
        versionString = versionString.toUpperCase().replaceAll(" ", "");
        if (versionString.startsWith(">="))
            compare = VersionCompare.GREATER_THAN_EQUAL;
        else if (versionString.startsWith(">"))
            compare = VersionCompare.GREATER_THAN;
        else if (versionString.startsWith("<="))
            compare = VersionCompare.LESS_THAN_EQUAL;
        else if (versionString.startsWith("<"))
            compare = VersionCompare.LESS_THAN;
        else
            compare = VersionCompare.EQUAL;

        versionString = versionString.replaceAll("[ ><=]", "");


        int marker = versionString.indexOf("-");
        String version;
        String status;
        if (marker == -1) {
            version = versionString;
            status = "alpha";
        } else {
            version = versionString.substring(0, marker);
            status = versionString.substring(marker + 1);
        }


        String[] versionParts = version.split("\\.");
        major = parseVersionPart(versionParts, 0);
        minor = parseVersionPart(versionParts, 1);
        patch = parseVersionPart(versionParts, 2);

        this.status = ReleaseStatus.valueOf(status.toUpperCase());


    }

    private int parseVersionPart(String[] versionParts, int index) {
        return versionParts.length > index ? Integer.parseInt(versionParts[index]) : 0;
    }

    public ZXVersion(int major, int minor, int patch, ReleaseStatus status) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.status = status;
        this.compare = null;
    }

    public ZXVersion(int major, int minor, int patch, ReleaseStatus status, VersionCompare compare) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.status = status;
        this.compare = compare;
    }

    /**
     * 与其他版本比较 检查是否支持
     *
     * @param version 被比较版本
     * @return 是否支持
     */
    public boolean isSupported(ZXVersion version) {
        return switch (compare) {
            case EQUAL -> version.getCode() == getCode();
            case LESS_THAN -> version.getCode() < getCode();
            case LESS_THAN_EQUAL -> version.getCode() <= getCode();
            case GREATER_THAN -> version.getCode() > getCode();
            case GREATER_THAN_EQUAL -> version.getCode() >= getCode();
        };
    }

    public static ZXVersion resolve(int versionCode) {
        return new ZXVersion(versionCode / 100000, versionCode / 1000 % 100, versionCode / 10 % 100,
                ReleaseStatus.values()[versionCode % 10]);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch + "-" + status.getSign();
    }

    public int getCode() {
        return major * 100000 + minor * 1000 + patch * 10 + status.ordinal();
    }

    public void printInfo() {
        System.out.println("Version: " + this + " Code: " + this.getCode());
        switch (this.status) {
            case RC -> ZXLogger.warning("当前为 [内部测试版本] 请不要泄漏软件到外部");
            case BETA -> ZXLogger.info("当前为 [提前预览版本] 如有问题请联系开发者");
            case ALPHA -> ZXLogger.info("当前为 [早期开发版本] 请谨慎测试软件功能");
            case STABLE -> ZXLogger.info("当前为 [稳定发布版本] 请尽情使用");
        }
    }

    /**
     * 发布状态
     */
    public enum ReleaseStatus {
        ALPHA("alpha"),
        BETA("beta"),
        RC("rc"),
        STABLE("stable");

        private final String sign;

        ReleaseStatus(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }
    }

    /**
     * 版本比较
     */
    public enum VersionCompare {
        /**
         * 大于 >
         */
        GREATER_THAN(">"),
        /**
         * 大于等于 >=
         */
        GREATER_THAN_EQUAL(">="),
        /**
         * 等于 ==
         */
        EQUAL(""),
        /**
         * 小于等于 <=
         */
        LESS_THAN_EQUAL("<="),
        /**
         * 小于 <
         */
        LESS_THAN("<");
        final String sign;

        VersionCompare(String sign) {
            this.sign = sign;
        }
    }

}
