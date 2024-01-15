import team.zxorg.core.ZXLogger;
import team.zxorg.core.ZXVersion;
import team.zxorg.loader.ExtensionLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class RunApp {
    public static ZXVersion VERSION = new ZXVersion(0, 0, 0, ZXVersion.ReleaseStatus.ALPHA);

    public static void main(String[] args) throws IOException {
        VERSION.printInfo();
        ZXLogger.info("扩展加载器初始化。");

        Path exceptionsPath=Path.of("./extensions");
        Files.list(exceptionsPath).forEach(path -> {
            try {
                ExtensionLoader.loadException(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
