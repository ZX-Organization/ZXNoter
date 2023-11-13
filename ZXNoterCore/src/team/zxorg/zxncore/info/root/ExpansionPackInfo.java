package team.zxorg.zxncore.info.root;

import java.nio.file.Path;

/**
 * 扩展包的配置信息
 */
public class ExpansionPackInfo {
    /**
     * 扩展包命名空间 如"zxnoter"
     */
    public String namespace;
    /**
     * 扩展包id 如"rightAngle"
     */
    public String id;
    /**
     * 扩展包作者 如"zedoCN&Xiang&more..."
     */
    public String author;

    /**
     * 扩展包版本 如"0.0.0-Alpha"
     */
    public String version;
    /**
     * 发布日期
     */
    public long dateReleased;
    /**
     * 扩展包格式版本 必须符合核心支持的版本
     */
    public long packFormat;
    /**
     * 扩展包默认使用的语言代码 如"zh-cn"
     */
    public String defaultLanguageCode;

}
