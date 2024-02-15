package team.zxorg.extensionloader.core;

import team.zxorg.extensionloader.event.ConfigEventListener;

public abstract class ConfigData {
    transient Configuration config;

    public void save() {
        config.save(this.getClass());
    }

    public void addEventListener(ConfigEventListener listener) {
        config.addEventListener(this.getClass(), listener);
    }

    public void removeEventListener(ConfigEventListener listener) {
        config.removeEventListener(this.getClass(), listener);
    }
}
