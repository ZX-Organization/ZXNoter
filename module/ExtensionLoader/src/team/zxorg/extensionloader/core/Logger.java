package team.zxorg.extensionloader.core;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.*;

public class Logger {
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[HH:mm:ss] ");
    public static final Formatter formatter = new Formatter() {
        @Override
        public String format(LogRecord record) {
            String sourceClassName = record.getSourceClassName();
            if (sourceClassName == null) {
                sourceClassName = "";
            }


            String prefix = String.format("%s[%s.%s(%d)/%s]: ",
                    simpleDateFormat.format(Date.from(record.getInstant())),
                    sourceClassName.substring(sourceClassName.lastIndexOf(".") + 1),
                    record.getSourceMethodName(),
                    record.getParameters()[0],
                    record.getLevel().getName()
            );

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
    };
    private static final Handler handler = new ConsoleHandler();

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());

    public static void addLoggerHandler(Handler handler) {
        logger.addHandler(handler);
    }

    static {
        Logger.initialize();

        // 设置日志记录级别为 DEBUG
    }

    public static void initialize() {
        handler.setFormatter(formatter);
        handler.setLevel(Level.ALL);
        logger.setUseParentHandlers(false); // 禁用父级处理程序
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);

        try {
            FileHandler fileHandler = new FileHandler("./latest.log");
            fileHandler.setFormatter(formatter);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            warning(Language.get("message.logger.error"));
        }

        System.setErr(new PrintStream(System.out) {
            @Override
            public void write(byte[] buf, int off, int len) {
                printSystemLog(buf, off, len, Level.WARNING);
            }
        });
        System.setOut(new PrintStream(System.out) {
            @Override
            public void write(byte[] buf, int off, int len) {
                printSystemLog(buf, off, len, CustomizeLevel.DEBUG);
            }
        });
        info(Objects.requireNonNullElse(Language.getOrNull("message.logger.initialize"),"Logger is initialized"));
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


    public static void log(Level level, String message) {
        log(level, message, 0);
    }

    public static void log(Level level, String message, int cutoff) {
        LogRecord record = new LogRecord(level, message);
        record.setSourceMethodName("UNKNOWN");
        StackTraceElement ste = getStackTraceElement(Logger.class.getName(), cutoff);
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
    public static StackTraceElement getStackTraceElement(String className, int cutoff) {
        if (className == null) {
            throw new IllegalArgumentException("className cannot be null");
        }
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
}