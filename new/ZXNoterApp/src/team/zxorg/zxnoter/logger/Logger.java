package team.zxorg.zxnoter.logger;

import org.jetbrains.annotations.NotNull;
import team.zxorg.zxnoter.api.core.ANSICode;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class Logger {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final Formatter formatter = new LoggerFormatter(true);
    private static final Formatter fileFormatter = new LoggerFormatter(false);
    private static final Handler handler = new ConsoleHandler() {
        {
            setOutputStream(System.out);
        }
    };
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());

    //自初始化
    static {
        Logger.initialize();
    }

    private static String styleLevel(Level level) {
        if (level == CustomizeLevel.DEBUG) {
            return ANSICode.applyStyle(level.getName(), ANSICode.MAGENTA);
        } else if (level == CustomizeLevel.INFO) {
            return ANSICode.applyStyle(level.getName(), ANSICode.WHITE);
        } else if (level == CustomizeLevel.WARNING) {
            return ANSICode.applyStyle(level.getName(), ANSICode.YELLOW, ANSICode.BLINK);
        } else if (level == CustomizeLevel.SEVERE) {
            return ANSICode.applyStyle(level.getName(), ANSICode.RED, ANSICode.BLINK_FAST, ANSICode.BOLD);
        }
        return ANSICode.applyStyle(level.getName(), ANSICode.RESET);
    }

    public static void addLoggerHandler(Handler handler) {
        logger.addHandler(handler);
    }

    /**
     * 初始化日志
     */
    private static void initialize() {
        handler.setFormatter(formatter);
        handler.setLevel(Level.ALL);
        logger.setUseParentHandlers(false); // 禁用父级处理程序
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);

        try {
            FileHandler fileHandler = new FileHandler("./latest.log");
            fileHandler.setFormatter(fileFormatter);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            warning("message.logger.error", "CORE");
        }

        System.setErr(new PrintStream(System.out) {
            @Override
            public void write(byte @NotNull [] buf, int off, int len) {
                printSystemLog(buf, off, len, Level.WARNING);
            }
        });
        System.setOut(new PrintStream(System.out) {
            @Override
            public void write(byte @NotNull [] buf, int off, int len) {
                printSystemLog(buf, off, len, CustomizeLevel.DEBUG);
            }
        });
    }

    private static void printSystemLog(byte[] buf, int off, int len, Level level) {
        // 处理输出的日志消息
        String message = new String(buf, off, len).trim();
        if (message.isEmpty())
            return;

        LogRecord record = new LogRecord(level, null);

        //处理sout的打印
        StackTraceElement stackTraceElement = getStackTraceElement("java.io.PrintStream", 0);
        if (stackTraceElement != null) {
            setLogRecord(record, stackTraceElement);
        }

        record.setMessage(message);
        logger.log(record);

    }

    private static void setLogRecord(LogRecord record, StackTraceElement ste) {
        record.setParameters(new Object[]{ste.getLineNumber()});
        record.setSourceClassName(ste.getClassName());
        record.setSourceMethodName(ste.getMethodName());
    }

    /**
     * 调试
     */
    public static void debug(Object message, String other) {
        log(CustomizeLevel.DEBUG, message.toString(), other);
    }

    /**
     * 信息
     */
    public static void info(Object message, String other) {
        log(Level.INFO, message.toString(), other);
    }

    /**
     * 警告
     */
    public static void warning(Object message, String other) {
        log(Level.WARNING, message.toString(), other);
    }



    /**
     * 严重
     */
    public static void severe(Object message, String other) {
        log(Level.SEVERE, message.toString(), other);
    }

    /**
     * 调试
     */
    public static void debug(String message, String other, int cutoff) {
        log(CustomizeLevel.DEBUG, message, other, cutoff);
    }

    /**
     * 信息
     */
    public static void info(String message, String other, int cutoff) {
        log(Level.INFO, message, other, cutoff);
    }

    /**
     * 警告
     */
    public static void warning(String message, String other, int cutoff) {
        log(Level.WARNING, message, other, cutoff);
    }

    /**
     * 严重
     */
    public static void severe(String message, String other, int cutoff) {
        log(Level.SEVERE, message, other, cutoff);
    }

    private static void log(Level level, String message, String other) {
        log(level, message, other, 0);
    }

    /**
     * 日志
     *
     * @param level   等级
     * @param message 消息
     * @param cutoff  截止
     */
    private static void log(Level level, String message, String other, int cutoff) {
        LogRecord record = new LogRecord(level, message);
        record.setSourceMethodName("UNKNOWN");
        StackTraceElement ste = getStackTraceElement(Logger.class.getName(), cutoff);
        assert ste != null;
        record.setSourceMethodName(ste.getMethodName());
        record.setParameters(new Object[]{ste.getLineNumber(), ste.getClassLoaderName()});
        record.setSourceClassName(ste.getClassName());
        logger.log(record);
    }

    /**
     * 追踪堆栈
     *
     * @param className 追踪类名
     * @param cutoff    追踪深度偏移
     * @return 堆栈信息
     */
    public static StackTraceElement getStackTraceElement(@NotNull String className, int cutoff) {
        boolean foundClassName = false;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement ste = stackTraceElements[i];
            String stackClassName = ste.getClassName();
            if (stackClassName.contains(className)) {
                foundClassName = true;
            } else if (foundClassName) {
                return stackTraceElements[i + cutoff];
            }
        }
        return null;
    }

    /**
     * 记录异常的堆栈追踪信息
     *
     * @param e 异常
     */
    public static void logExceptionStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
        }
        warning(sw.toString(), "CORE", 0);
    }

    /**
     * 获取异常的堆栈追踪信息
     *
     * @param e 异常
     */
    public static String getExceptionStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
        }
        return sw.toString();
    }

    /**
     * 记录堆栈追踪信息
     */
    public static void logStackTrace() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        warning("----StackTrace----", "CORE", 0);
        for (int i = 2; i < elements.length; i++) {
            warning("at " + elements[i].toString(), "CORE", 0);
        }
    }

    private static final class LoggerFormatter extends Formatter {
        private final boolean enabledANSI;

        public LoggerFormatter(boolean enabledANSI) {
            this.enabledANSI = enabledANSI;
        }

        @Override
        public String format(LogRecord record) {
            String sourceClassName = record.getSourceClassName();
            if (sourceClassName == null) {
                sourceClassName = "";
            }
            String other = (String) record.getParameters()[1];
            StringBuilder prefix = new StringBuilder();
            if (enabledANSI)
                prefix.append(ANSICode.RESET)
                        .append('[')
                        .append(ANSICode.applyStyle(simpleDateFormat.format(Date.from(record.getInstant())), ANSICode.WHITE, ANSICode.FAINT))
                        .append("] [")
                        .append(ANSICode.applyStyle(other, ANSICode.BLUE))
                        .append("] [")
                        .append(ANSICode.GREEN)
                        .append(sourceClassName.substring(sourceClassName.lastIndexOf(".") + 1))
                        .append('.')
                        .append(record.getSourceMethodName())
                        .append(ANSICode.RESET)
                        .append('(')
                        .append(ANSICode.applyStyle(record.getParameters()[0], ANSICode.BRIGHT_GREEN))
                        .append(")/")
                        .append(styleLevel(record.getLevel()))
                        .append("]")
                        .append(ANSICode.BRIGHT_WHITE)
                        .append(": ")
                        .append(ANSICode.RESET)
                        ;
            else {
                prefix.append('[')
                        .append(simpleDateFormat.format(Date.from(record.getInstant())))
                        .append("] [")
                        .append(other)
                        .append("] [")
                        .append(sourceClassName.substring(sourceClassName.lastIndexOf(".") + 1))
                        .append('.')
                        .append(record.getSourceMethodName())
                        .append('(')
                        .append(record.getParameters()[0])
                        .append(")/")
                        .append(record.getLevel().getName())
                        .append("]")
                        .append(": ")
                ;
            }

            String message = record.getMessage();
            if (message.contains("\n")) {
                String[] messageLines = message.split("\n");

                StringBuilder sb = new StringBuilder();
                for (String messageLine : messageLines) {
                    sb.append(prefix).append(messageLine.trim()).append("\n");
                }
                return sb.toString();
            }

            return prefix + message + "\n";
        }
    }

    private static final class CustomizeLevel extends Level {
        public static final CustomizeLevel DEBUG = new CustomizeLevel("DEBUG", 200);

        private CustomizeLevel(String name, int value) {
            super(name, value);
        }
    }
}