package team.zxorg.zxnoter.extension;

import team.zxorg.zxnoter.api.extension.IExtensionLogger;
import team.zxorg.zxnoter.logger.Logger;

public class ExtensionLogger implements IExtensionLogger {
    String namespace;

    @Override
    public void debug(Object message) {
        Logger.debug(message,namespace);
    }

    @Override
    public void info(Object message) {
        Logger.info(message,namespace);
    }

    @Override
    public void warning(Object message) {
        Logger.warning(message,namespace);
    }

    @Override
    public void severe(Object message) {
        Logger.severe(message,namespace);
    }
}
