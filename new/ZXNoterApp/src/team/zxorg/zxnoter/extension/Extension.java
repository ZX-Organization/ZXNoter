package team.zxorg.zxnoter.extension;

import team.zxorg.zxnoter.api.core.IEventListener;
import team.zxorg.zxnoter.api.extension.IApp;
import team.zxorg.zxnoter.api.extension.IExtension;
import team.zxorg.zxnoter.api.extension.IExtensionLogger;
import team.zxorg.zxnoter.api.version.IVersion;

import java.util.List;

public class Extension implements IExtension {
    @Override
    public String getId() {
        return "";
    }

    @Override
    public IVersion getVersion() {
        return null;
    }

    @Override
    public List<IExtension> getDependencies() {
        return List.of();
    }

    @Override
    public IExtensionLogger getLogger() {
        return null;
    }

    @Override
    public IApp getApp() {
        return null;
    }

    @Override
    public void registerEventListener(String eventType, IEventListener listener) {

    }

    @Override
    public void triggerEvent(String eventType, Object eventData) {

    }

    @Override
    public String getName() {
        return "";
    }
}
