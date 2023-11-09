package team.zxorg.zxnoter.ui.main.stage.status;

import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.ZXColor;
import team.zxorg.zxnoter.ui.component.ZXIcon;
import team.zxorg.zxnoter.ui.component.ZXLabel;
import team.zxorg.zxnoter.ui.component.ZXStatus;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ConsoleStatus extends ZXStatus {
    public static final String infoIcon = "development.terminal-box";
    public static final String warIcon = "system.information";
    public static final String errIcon = "system.error-warning";

    ZXIcon consoleStatusIcon = new ZXIcon(infoIcon, ZXColor.GRAY, 16);

    public ConsoleStatus() {

        ZXLogger.addLoggerHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                Level level = record.getLevel();
                if (level.equals(Level.SEVERE)) {
                    //info.setText(record.getMessage());
                    consoleStatusIcon.setIconKey(errIcon);
                    consoleStatusIcon.setColor(ZXColor.RED);
                } else if (level.equals(Level.WARNING)) {
                    //info.setText(record.getMessage());
                    consoleStatusIcon.setIconKey(warIcon);
                    consoleStatusIcon.setColor(ZXColor.YELLOW);

                }
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        });
        getChildren().addAll(consoleStatusIcon, new ZXLabel("status-bar.console.title"));
    }
}
