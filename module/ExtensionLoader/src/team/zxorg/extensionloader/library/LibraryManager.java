package team.zxorg.extensionloader.library;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.StopWatch;
import team.zxorg.extensionloader.core.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static team.zxorg.extensionloader.library.LibraryManager.PlatformType.*;

public class LibraryManager {
    private static final PlatformType platform;

    static {
        boolean isArm64 = System.getProperty("os.arch").contains("aarch64");
        String suffix = isArm64 ? "-aarch64" : "";
        if (SystemUtils.IS_OS_MAC) {
            platform = isArm64 ? MAC_ARM : MAC;
        } else if (SystemUtils.IS_OS_LINUX) {
            platform = isArm64 ? LINUX_ARM : LINUX;
        } else if (SystemUtils.IS_OS_WINDOWS) {
            platform = WINDOWS;
        } else {
            platform = ALL;
        }
    }


    /**
     * 加载指定目录下的所有库
     *
     * @param dirPath 指定目录
     * @return 加载完库的ClassLoader
     */
    public static URLClassLoader loadingAllLibrary(ClassLoader parent, Path dirPath) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        HashMap<String, URL> libraryMap = new HashMap<>();
        try (Stream<Path> pathStream = Files.walk(dirPath)) {
            Iterator<Path> iterator = pathStream.iterator();
            while (iterator.hasNext()) {
                Path file = iterator.next();
                if (Files.isDirectory(file))
                    continue;
                String name = file.getFileName().toString();
                if (!name.endsWith(".jar"))
                    continue;
                PlatformType libPlatform = getPlatform(name.substring(0, name.lastIndexOf(".")));
                if (libPlatform != ALL)
                    if (libPlatform != platform)
                        continue;
                if (libraryMap.containsKey(name)) {
                    Logger.warning("库 " + name + " 重复载入");
                    continue;
                }
                Logger.info("载入库: " + name);
                libraryMap.put(name, file.toUri().toURL());
            }
        } catch (IOException e) {
            Logger.warning("载入库时发生异常: " + e);
        }

        URL[] jarUrl = new URL[libraryMap.size()];
        stopWatch.stop();
        Logger.info("共计 " + jarUrl.length + " 个库载入完成 用时: " + stopWatch.getTime() + " ms");
        libraryMap.values().toArray(jarUrl);
        return new URLClassLoader(jarUrl, parent);
    }

    public enum PlatformType {
        MAC_ARM("mac", "aarch64"),
        MAC("mac"),
        LINUX_ARM("linux", "aarch64"),
        LINUX("linux"),
        WINDOWS("windows"),
        ALL("");
        private final String[] keywords;

        PlatformType(String... keywords) {
            this.keywords = keywords;
        }
    }

    private static PlatformType getPlatform(String libraryName) {
        List<String> libraryNameParts = new ArrayList<>(List.of(libraryName.split("-")));
        for (PlatformType platformType : PlatformType.values()) {
            boolean isMatch = true;
            for (String keyword : platformType.keywords) {
                if (!libraryNameParts.contains(keyword)) {
                    isMatch = false;
                    break;
                }
            }
            if (isMatch)
                return platformType;
        }
        return ALL;
    }
}
