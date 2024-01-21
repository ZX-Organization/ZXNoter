package team.zxorg.extendedtemplate;

import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionManager;

public class ExtensionTemplate implements ExtensionEntrypoint {
    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {
        Logger.info(extension.getLanguage("message.hello"));
    }
}
