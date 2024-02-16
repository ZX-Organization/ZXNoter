package team.zxorg.fxcl;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.util.Logging;
import javafx.application.Platform;
import org.apache.commons.lang3.time.StopWatch;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.extension.Extension;
import team.zxorg.extensionloader.extension.ExtensionEntrypoint;
import team.zxorg.extensionloader.extension.ExtensionManager;
import team.zxorg.fxcl.resource.IconManager;
import team.zxorg.fxcl.resource.ImageManager;

public class FXCL implements ExtensionEntrypoint {
    private static final String LANG = "extension.fxComponentLibrary.";

    @Override
    public void onLoaded(Extension extension, ExtensionManager manager) {
        StopWatch stopWatch = new StopWatch();

        //屏蔽javafx歌姬初始化时的异常
        Logger.info(extension.getLanguage(LANG + "javaFX.initializing"));
        stopWatch.start();
        Logging.getJavaFXLogger().disableLogging();
        PlatformImpl.startup(() -> {
            //再次开启javafx日志
            Logging.getJavaFXLogger().enableLogging();
        });
        PlatformImpl.setApplicationName(FXCL.class);
        stopWatch.stop();
        Logger.info(extension.getLanguage(LANG + "javaFX.initialized", stopWatch.getTime()));
        IconManager.reloadIcons();
        ImageManager.reloadImages();

    }
}
