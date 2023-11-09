package team.zxorg.zxnoter.config.configuration.sub;

/**
 * 不是很详细的项目属性
 */
public class MiniProjectCfg {
    /**
     * 项目路径 在项目目录下则是相对路径，其他位置则是绝对路径
     * 由项目目录 '[work]' 互相转义 绝对路径
     */
    public String path;
    /**
     * 项目的标题，默认是项目的目录名
     */
    public String title;
}
