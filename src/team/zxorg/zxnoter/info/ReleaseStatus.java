package team.zxorg.zxnoter.info;

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
