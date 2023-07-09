package team.zxorg.zxnoter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ZXLogger {
    public static final Logger logger = Logger.getLogger("ZXLogger");

    /**
     * 记录一般信息性消息
     * @param message 消息内容
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * 记录警告信息
     * @param message 消息内容
     */
    public static void warning(String message) {
        logger.warning(message);
    }

    /**
     * 记录严重错误信息
     * @param message 消息内容
     */
    public static void severe(String message) {
        logger.severe(message);
    }

    /**
     * 记录调试信息
     * @param message 消息内容
     */
    public static void debug(String message) {
        logger.fine(message);
    }

    /**
     * 记录更详细的调试信息
     * @param message 消息内容
     */
    public static void finer(String message) {
        logger.finer(message);
    }

    /**
     * 记录最详细的调试信息
     * @param message 消息内容
     */
    public static void finest(String message) {
        logger.finest(message);
    }
}