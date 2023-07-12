package team.zxorg.zxnoter.resource;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.shape.SVGPath;
import team.zxorg.zxnoter.resource.pack.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GlobalResources {


    /**
     * 全局样式资源
     */
    private static final ObservableList<String> styleResources = FXCollections.observableArrayList();
    /**
     * 全局语言资源
     */
    private static final HashMap<String, StringProperty> languageResources = new HashMap<>();
    /**
     * 全局图标资源
     */
    private static final HashMap<String, ObjectProperty<SVGPath>> iconResources = new HashMap<>();

    public static StringProperty getLanguageContent(String key) {
        return languageResources.get(key);
    }

    public static ObjectProperty<SVGPath> getIcon(String key) {
        return iconResources.get(key);
    }

    /**
     * 为Scene重新应用样式
     *
     * @param scene 被设置的Scene
     */
    public static void bindSceneStyle(Scene scene) {
        ObservableList<String> lists = scene.getStylesheets();
        lists.clear();
        String[] stylePaths = new String[styleResources.size()];
        styleResources.toArray(stylePaths);
        lists.addAll(stylePaths);

        styleResources.addListener((ListChangeListener<String>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    lists.addAll(c.getAddedSubList());
                } else if (c.wasRemoved()) {
                    lists.removeAll(c.getRemoved());
                }
            }
        });
    }

    public static void reloadResources(ArrayList<BaseResourcePack> globalResources) {
        styleResources.clear();//清除样式资源

        for (BaseResourcePack pack : globalResources) {
            if (pack instanceof LanguageResourcePack languageResourcePack) {
                for (Map.Entry<String, String> language : languageResourcePack.getLanguages().entrySet()) {
                    StringProperty languageContent = languageResources.get(language.getKey());
                    if (languageContent == null) {
                        languageContent = new SimpleStringProperty();
                        languageResources.put(language.getKey(), languageContent);
                    }
                    languageContent.set(language.getValue());
                }
            } else if (pack instanceof IconResourcePack iconResourcePack) {
                for (Map.Entry<String, SVGPath> svgPath : iconResourcePack.getIcons().entrySet()) {
                    ObjectProperty<SVGPath> svgProperty = iconResources.get(svgPath.getKey());
                    if (svgProperty == null) {
                        svgProperty = new SimpleObjectProperty<>();
                        iconResources.put(svgPath.getKey(), svgProperty);
                    }
                    svgProperty.set(svgPath.getValue());
                }
            } else if (pack instanceof LayoutResourcePack layoutResourcePack) {
                for (Path file : layoutResourcePack.getCssFiles()) {
                    styleResources.add(file.toUri().toString());
                }
            }else if (pack instanceof ColorResourcePack colorResourcePack) {
                for (Path file : colorResourcePack.getCssFiles()) {
                    styleResources.add(file.toUri().toString());
                }
            }
        }
    }
}
