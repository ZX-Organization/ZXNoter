package team.zxorg.extensionloader.library;

import team.zxorg.extensionloader.core.Language;
import team.zxorg.extensionloader.core.Logger;
import team.zxorg.extensionloader.core.PlatformType;
import team.zxorg.extensionloader.core.StopWatch;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static team.zxorg.extensionloader.core.PlatformType.ALL;
import static team.zxorg.extensionloader.core.PlatformType.CURRENT_PLATFORM;


public class LibraryManager {


    /**
     * 加载指定目录下的所有库
     *
     * @param dirPath 指定目录
     * @return 加载完库的ClassLoader
     */
    public static ClassLoader loadingAllLibrary(ClassLoader parent, Path dirPath) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        HashMap<String, URL> libraryMap = new HashMap<>();
        Path file = null;
        String name = null;
        Logger.info(Language.get("message.library.loadingAll"));
        try (Stream<Path> pathStream = Files.walk(dirPath)) {
            Iterator<Path> iterator = pathStream.iterator();
            while (iterator.hasNext()) {
                file = iterator.next();
                if (Files.isDirectory(file))
                    continue;
                name = file.getFileName().toString();
                if (!name.endsWith(".jar"))
                    continue;
                PlatformType libPlatform = getPlatform(name.substring(0, name.lastIndexOf(".")));
                if (libPlatform != ALL)
                    if (libPlatform != CURRENT_PLATFORM)
                        continue;
                if (libraryMap.containsKey(name)) {
                    Logger.warning("库 " + name + " 重复载入");
                    Logger.info(Language.get("message.library.repeat", file));
                    continue;
                }
                Logger.info(Language.get("message.library.loading",file));
                libraryMap.put(name, file.toUri().toURL());
            }
        } catch (IOException e) {
            Logger.info(Language.get("message.library.error", file, e));
            Logger.logExceptionStackTrace(e);
        }

        URL[] jarUrl = new URL[libraryMap.size()];
        libraryMap.values().toArray(jarUrl);
        ClassLoader classLoader = new URLClassLoader(jarUrl, parent);
        stopWatch.stop();
        Logger.info(Language.get("message.library.loaded", jarUrl.length, stopWatch.getTime()));
        return classLoader;
    }


    private static PlatformType getPlatform(String libraryName) {
        Set<String> libraryNameParts = new HashSet<>(Arrays.asList(libraryName.toLowerCase().split("-")));
        for (PlatformType platformType : PlatformType.values()) {
            Set<String> keywords = platformType.getKeywords();
            if (libraryNameParts.containsAll(keywords)) {
                return platformType;
            }
        }
        return PlatformType.ALL;
    }

}
