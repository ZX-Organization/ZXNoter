package team.zxorg.fxcl.resource;

import javafx.beans.property.ObjectProperty;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;

public class IconManager {
    public static HashMap<String, ObjectProperty<Object>> icons = new HashMap<>();

    public static void loadIconPack(ClassLoader classLoader, String packName) {
        try {
            Enumeration<URL> enumeration = classLoader.getResources(packName);
            while (enumeration.hasMoreElements()) {
                System.out.println(enumeration.nextElement());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
