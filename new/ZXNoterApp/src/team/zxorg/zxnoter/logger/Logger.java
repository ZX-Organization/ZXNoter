package team.zxorg.zxnoter.logger;

import org.jetbrains.annotations.NotNull;
import team.zxorg.zxnoter.util.ANSICode;

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

    static {
        Logger.initialize();

        // 设置日志记录级别为 DEBUG
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
    public static void initialize() {
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
            //warning(Language.get("message.logger.error"));
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
        //info(Objects.requireNonNullElse(Language.getOrNull("message.logger.initialize"),"Logger is initialized"));
    }

    private static void printSystemLog(byte[] buf, int off, int len, Level level) {
        // 处理输出的日志消息
        String message = new String(buf, off, len).trim();
        if (message.isEmpty())
            return;

        LogRecord record = new LogRecord(level, null);
        /*if (messageLines.length > 1) {
            String[] p = messageLines[0].split(" ");
            if (p.length > 2) {
                record.setParameters(new Object[]{-1});
                record.setSourceClassName(p[p.length - 2]);
                record.setSourceMethodName(p[p.length - 1].trim());
                record.setMessage(message.substring(messageLines[0].length() + 1));
                logger.log(record);
                return;
            }
        }*/

        //处理sout的打印
        StackTraceElement stackTraceElement = getStackTraceElement("java.io.PrintStream", 0);
        if (stackTraceElement != null) {
            //record.setLevel(Level.FINE);
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
    public static void debug(Object message) {
        log(CustomizeLevel.DEBUG, message.toString());
    }

    /**
     * 信息
     */
    public static void info(Object message) {
        log(Level.INFO, message.toString());
    }

    /**
     * 警告
     */
    public static void warning(Object message) {
        log(Level.WARNING, message.toString());
    }

    /**
     * 严重
     */
    public static void severe(Object message) {
        log(Level.SEVERE, message.toString());
    }

    /**
     * 信息
     */
    public static void debug(String message, int cutoff) {
        log(CustomizeLevel.DEBUG, message, cutoff);
    }

    /**
     * 信息
     */
    public static void info(String message, int cutoff) {
        log(Level.INFO, message, cutoff);
    }

    /**
     * 警告
     */
    public static void warning(String message, int cutoff) {
        log(Level.WARNING, message, cutoff);
    }

    /**
     * 严重
     */
    public static void severe(String message, int cutoff) {
        log(Level.SEVERE, message, cutoff);
    }

    private static void log(Level level, String message) {
        log(level, message, 0);
    }

    /**
     * 日志
     *
     * @param level   等级
     * @param message 消息
     * @param cutoff  截止
     */
    private static void log(Level level, String message, int cutoff) {
        LogRecord record = new LogRecord(level, message);
        record.setSourceMethodName("UNKNOWN");
        StackTraceElement ste = getStackTraceElement(Logger.class.getName(), cutoff);
        assert ste != null;
        record.setSourceMethodName(ste.getMethodName());
        record.setParameters(new Object[]{ste.getLineNumber()});
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
        log(Level.WARNING, sw.toString(), 0);
    }

    /**
     * 记录堆栈追踪信息
     */
    public static void logStackTrace() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        log(Level.WARNING, "----StackTrace----", 0);
        for (int i = 2; i < elements.length; i++) {
            log(Level.WARNING, "at " + elements[i].toString(), 0);
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

            StringBuilder prefix = new StringBuilder();
            if (enabledANSI)
                prefix.append(ANSICode.RESET)
                        .append('[')
                        .append(ANSICode.applyStyle(simpleDateFormat.format(Date.from(record.getInstant())), ANSICode.WHITE, ANSICode.FAINT))
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
                for (int i = 0; i < messageLines.length; i++) {
                    sb.append(prefix).append(messageLines[i].trim()).append("\n");
                }
                return sb.toString();
            }

            return prefix + message + "\n";
        }
    }

    private static class CustomizeLevel extends Level {
        public static final CustomizeLevel DEBUG = new CustomizeLevel("DEBUG", 200);

        protected CustomizeLevel(String name, int value) {
            super(name, value);
        }
    }
}