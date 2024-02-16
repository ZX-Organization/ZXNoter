package team.zxorg.extensionloader.core;

import team.zxorg.extensionloader.event.ConfigEventListener;

public abstract class ConfigData {
    transient Configuration config;
    transient boolean needSave;

    public void save() {
        config.save(this.getClass());
    }

    public void addEventListener(ConfigEventListener listener) {
        config.addEventListener(this.getClass(), listener);
    }

    public void removeEventListener(ConfigEventListener listener) {
        config.removeEventListener(this.getClass(), listener);
    }

    public void needSave() {
        needSave = true;
    }

    /**
     * 被载入后执行的代码
     */
    protected void loaded() {

    }
}
