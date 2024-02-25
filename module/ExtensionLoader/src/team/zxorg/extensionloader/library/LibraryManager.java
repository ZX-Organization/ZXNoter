package team.zxorg.extensionloader.library;

import org.apache.commons.lang3.SystemUtils;

import java.nio.file.Path;

public class LibraryManager {
    public static void loadLibrary(Path path) {

    }

    private static String determineLibraryName() {
        boolean isArm64 = System.getProperty("os.arch").contains("aarch64");

        if (SystemUtils.IS_OS_MAC) {

        } else if (SystemUtils.IS_OS_LINUX) {

        } else if (SystemUtils.IS_OS_WINDOWS) {
        } else {
            throw new UnsupportedOperationException("Unsupported operating system");
        }
        return "";
    }
}
