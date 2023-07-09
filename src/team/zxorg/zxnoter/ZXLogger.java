package team.zxorg.zxnoter;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class ZXLogger {


    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[HH:mm:ss] ");
    private static final Formatter formatter = new Formatter() {
        @Override
        public String format(LogRecord record) {
            return simpleDateFormat.format(Date.from(record.getInstant())) + "[" + record.getSourceMethodName() + "/" + record.getLevel() + "]: " + record.getMessage() + "\n";
        }
    };
    public static final Logger logger = Logger.getLogger("ZXLogger");

    static {
        Handler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        logger.setUseParentHandlers(false); // 禁用父级处理程序
        logger.addHandler(handler);
    }
}