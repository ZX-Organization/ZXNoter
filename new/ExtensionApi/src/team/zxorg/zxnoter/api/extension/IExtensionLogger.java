package team.zxorg.zxnoter.api.extension;

public interface IExtensionLogger {

    /**
     * 调试日志
     *
     * @param message 消息内容
     */
    void debug(Object message);

    /**
     * 信息日志
     *
     * @param message 消息内容
     */
    void info(Object message);

    /**
     * 警告日志
     *
     * @param message 消息内容
     */
    void warning(Object message);

    /**
     * 严重日志
     *
     * @param message 消息内容
     */
    void severe(Object message);

}
