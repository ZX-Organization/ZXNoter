package team.zxorg.loader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;

public class ExtensionLoader {
    public static InputStream getResource(String res) {
        return ExtensionLoader.class.getResourceAsStream(res);
    }

    public static Extension loadException(Path path) throws IOException {


        Extension exception = new Extension(path.toFile().toURL());
        exception.initialize();
        //exception.info=
        return exception;
    }
}
