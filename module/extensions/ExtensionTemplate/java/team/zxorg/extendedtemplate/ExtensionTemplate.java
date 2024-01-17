package team.zxorg.extendedtemplate;

import team.zxorg.api.ExtensionEntrypoint;
import team.zxorg.core.ZXLogger;
import team.zxorg.extension.Extension;
import team.zxorg.extension.ExtensionManager;
import team.zxorg.fxcl.FXCL;

public class ExtensionTemplate implements ExtensionEntrypoint {
    @Override
    public void onInitialize(Extension extension, ExtensionManager manager) {
        ZXLogger.info("扩展模板 初始化完毕.");
        Extension fx = manager.getExtension("fx-component-library");
        new FXCL().onInitialize(extension, manager);
        System.out.println(getClass().getClassLoader());
    }
}
