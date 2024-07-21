package team.zxorg.zxnoter.api.version;

public interface IVersion extends Comparable<IVersion> {

    /**
     * 获取版本代码
     *
     * @return 版本代码
     */
    long getCode();

    /**
     * 获取主版本号
     *
     * @return 主版本号
     */
    int getMajor();

    /**
     * 获取次版本号
     *
     * @return 次版本号
     */
    int getMinor();

    /**
     * 获取补丁号
     *
     * @return 补丁号
     */
    int getPatch();

    /**
     * 获取发布状态
     *
     * @return 发布状态
     */
    VersionReleaseStatus getReleaseStatus();
}
