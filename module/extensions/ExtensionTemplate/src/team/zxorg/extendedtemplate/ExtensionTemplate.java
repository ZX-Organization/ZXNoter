package team.zxorg.extendedtemplate;

import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.core.ZXLogger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionManager;

public class ExtensionTemplate implements ExtensionEntrypoint {
    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {
        ZXLogger.info("扩展模板 初始化完毕.");
        Extension fx = manager.getExtension("fx-component-library");
        //new FXCL().onInitialize(extension, manager);
        //System.out.println(getClass().getClassLoader());
    }
}
