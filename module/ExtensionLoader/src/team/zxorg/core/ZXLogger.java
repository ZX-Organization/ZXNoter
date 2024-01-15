package team.zxorg.core;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class ZXLogger {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[HH:mm:ss] ");
    private static final Formatter formatter = new Formatter() {
        @Override
        public String format(LogRecord record) {
            String sourceClassName = record.getSourceClassName();
            if (sourceClassName == null) {
                sourceClassName = "";
            }
            return simpleDateFormat.format(Date.from(record.getInstant())) + "[" + sourceClassName.substring(sourceClassName.lastIndexOf(".") + 1) + "." + record.getSourceMethodName() + "/" + record.getLevel() + "]: " + record.getMessage() + "\n";
        }
    };
    private static final Handler handler = new ConsoleHandler();

    private static final Logger logger = Logger.getLogger("team.zxorg.core.ZXLogger");

    public static void addLoggerHandler(Handler handler) {
        logger.addHandler(handler);
    }

    static {
        ZXLogger.initialize();
    }

    public static void initialize() {
        handler.setFormatter(formatter);
        logger.setUseParentHandlers(false); // 禁用父级处理程序
        logger.addHandler(handler);
        try {
            FileHandler fileHandler = new FileHandler("./latest.log");
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.warning("日志文件IO异常。");
        }


        PrintStream printErr = new PrintStream(System.err) {
            @Override
            public void write(byte[] buf, int off, int len) {
                printSystemLog(buf, off, len, OutType.ERROR);
            }
        };
        PrintStream printOut = new PrintStream(System.out) {
            @Override
            public void write(byte[] buf, int off, int len) {
                printSystemLog(buf, off, len, OutType.STANDARD);
            }
        };

        System.setErr(printErr);
        System.setOut(printOut);
        System.out.println();
        info("ZX日志系统 初始化完毕");
    }

    private static void printSystemLog(byte[] buf, int off, int len, OutType outType) {
        // 处理输出的日志消息
        String message = new String(buf, off, len).trim();
        if (message.equals(""))
            return;
        String[] messageLines = message.split("\n");
        /**
         * 7月 10, 2023 2:28:17 下午 com.sun.javafx.application.PlatformImpl startup
         * 警告: Unsupported JavaFX configuration: classes were loaded from 'unnamed module @626b2d4a'
         */

        LogRecord record = new LogRecord(outType.level, null);
        if (messageLines.length > 1) {
            String[] p = messageLines[0].split(" ");
            if (p.length > 2) {
                record.setSourceClassName(p[p.length - 2]);
                record.setSourceMethodName(p[p.length - 1].trim());
                record.setMessage(message.substring(messageLines[0].length() + 1));
                ZXLogger.logger.log(record);
                return;
            }
        }

        boolean isFind = false;
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            if (stackTraceElement.getClassName().contains("java.io.PrintStream")) {
                isFind = true;
            } else {
                if (isFind) {
                    record.setSourceClassName(stackTraceElement.getClassName());
                    record.setSourceMethodName(stackTraceElement.getMethodName());
                    record.setMessage(message);
                }
                isFind = false;
            }
        }
        ZXLogger.logger.log(record);
    }

    /**
     * 信息
     */
    public static void info(String message) {
        log(Level.INFO, message);
    }

    /**
     * 警告
     */
    public static void warning(String message) {
        log(Level.WARNING, message);
    }

    /**
     * 严重
     */
    public static void severe(String message) {
        log(Level.SEVERE, message);
    }

    public static void log(Level level, String message) {
        LogRecord record = new LogRecord(level, message);
        record.setSourceMethodName("unknown");
        boolean isFind = false;
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {

            if (stackTraceElement.getClassName().contains(ZXLogger.class.getPackageName() + ".ZXLogger")) {
                isFind = true;
            } else {
                if (isFind) {
                    record.setSourceMethodName(stackTraceElement.getMethodName());
                    record.setSourceClassName(stackTraceElement.getClassName());
                }
                isFind = false;
            }
        }

        logger.log(record);
    }


    private enum OutType {
        ERROR(Level.WARNING), STANDARD(Level.INFO);
        public final Level level;

        OutType(Level level) {
            this.level = level;
        }
    }
}