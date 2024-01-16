import team.zxorg.core.ZXLogger;
import team.zxorg.core.ZXVersion;
import team.zxorg.extension.ExtensionManager;

import java.io.IOException;
import java.nio.file.Path;

public class RunApp {
    public static ZXVersion VERSION = new ZXVersion(0, 0, 0, ZXVersion.ReleaseStatus.ALPHA);

    public static void main(String[] args) throws IOException {
        VERSION.printInfo();
        ZXLogger.info("扩展加载器初始化。");
        ExtensionManager extensionManager = new ExtensionManager();


        Path exceptionsPath = Path.of("./extensions");
        extensionManager.loadExtensions(exceptionsPath);

    }
}
