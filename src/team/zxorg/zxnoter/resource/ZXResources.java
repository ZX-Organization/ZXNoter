package team.zxorg.zxnoter.resource;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.pack.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

public class ZXResources {
    /**
     * 载入的资源包
     */
    public static final HashMap<String, ResourcePack> loadedResourcePackMap = new HashMap<>();
    public static final ArrayList<BaseResourcePack> usedResources = new ArrayList<>();



    /*public static SVGPath getIconPane(String key) {
        SVGPath svgPath;
        for (BaseResourcePack baseResourcePack : globalResources.get(ResourceType.icon)) {
            if (baseResourcePack instanceof IconResourcePack iconResourcePack) {
                svgPath = iconResourcePack.getIcon(key);
                return svgPath;
            }
        }
        return null;
    }*/

   /* public static Pane getIconPane(String key, double size) {
        Pane pane = new Pane();
        SVGPath svgPath;
        for (BaseResourcePack baseResourcePack : globalResources.get(ResourceType.icon)) {
            if (baseResourcePack instanceof IconResourcePack iconResourcePack) {
                svgPath = iconResourcePack.getIcon(key);
                pane.setShape(svgPath);
                pane.getStyleClass().add("icon");
                pane.setPrefSize(size, size);
                pane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                return pane;
            }
        }
        return null;
    }*/

    /*public static String getLanguageContent(String key) {
        String content;
        for (BaseResourcePack baseResourcePack : globalResources.get(ResourceType.language)) {
            if (baseResourcePack instanceof LanguageResourcePack languageResourcePack) {
                content = languageResourcePack.getLanguageContent(key);
                if (content != null)
                    return content;
            }
        }
        return null;
    }*/

    public static final Image LOGO;
    public static final Image LOGO_X26;
    public static final Image UNKNOWN;

    static {
        ZXLogger.info("载入基本资源");
        try {
            LOGO = new Image(ZXResources.class.getResource("/base/logo.png").toURI().toString());
            LOGO_X26 = new Image(ZXResources.class.getResource("/base/logo-x26.png").toURI().toString());
            UNKNOWN = new Image(ZXResources.class.getResource("/base/Unknown.png").toURI().toString());
        } catch (Exception e) {
            ZXLogger.severe("载入基本资源发生异常");
            throw new RuntimeException(e);
        }
    }


    public static Pane getIconPane(String key, double size, ZXColor color) {
        Pane pane = new Pane();
        pane.shapeProperty().bind(GlobalResources.getIcon(key));
        pane.setPrefSize(size, size);
        if (color != null)
            pane.getStyleClass().add("bg-" + color);
        return pane;
    }

    /**
     * 清除资源包
     */
    public static void clearPacks() {
        ZXLogger.info("清除资源包列表");
        loadedResourcePackMap.clear();
    }

    /**
     * 搜索资源包目录
     *
     * @param resourcesDiv 资源包目录
     */
    public static void searchPacks(Path resourcesDiv) {
        if (!Files.exists(resourcesDiv)) {
            ZXLogger.warning("本地资源包目录不存在");
            return;
        }

        try (Stream<Path> packPaths = Files.list(resourcesDiv)) {
            Iterator<Path> iterator = packPaths.iterator();//资源包迭代器
            while (iterator.hasNext()) {//枚举所有资源包
                loadPack(iterator.next());
            }
        } catch (IOException e) {
            ZXLogger.warning("资源包枚举中断");
        }
    }

    /**
     * 载入资源包
     *
     * @param resourcePackDiv 资源包的目录
     */
    public static void loadPack(Path resourcePackDiv) {
        if (Files.exists(resourcePackDiv.resolve("resource-pack.json"))) {//如果资源包有引导文件
            ResourcePack resourcePack = new ResourcePack(resourcePackDiv);
            ResourcePack repeatPack = loadedResourcePackMap.get(resourcePack.getId());
            if (repeatPack == null) {//检查资源id是否重复
                ZXLogger.info("载入资源包 " + resourcePack.getId());
                loadedResourcePackMap.put(resourcePack.getId(), resourcePack);
            } else {
                ZXLogger.warning("资源包id冲突 现有 " + repeatPack + " 冲突 " + resourcePack);
            }
        } else {
            ZXLogger.warning("资源包缺失引导 " + resourcePackDiv);
        }
    }

    /**
     * 重载全局资源 (读取配置文件为资源包索引资源)
     */
    public static void reloadGlobalResource() {
        usedResources.clear();
        ZXLogger.info("载入引用的资源包列表");

        for (String ids : UserPreference.getUsedResources()) {
            String packId;
            ResourceType type;
            String resourceId;
            {
                String[] id = ids.split("\\.");
                if (id.length != 3) {
                    ZXLogger.warning("载入 " + ids + " id分隔异常");
                    continue;
                }

                packId = id[0];
                type = ResourceType.valueOf(id[1]);
                resourceId = id[2];
            }


            ResourcePack resourcePack = loadedResourcePackMap.get(packId);
            if (resourcePack == null) {
                ZXLogger.warning("载入 " + ids + " 资源包不存在");
                continue;
            }

            BaseResourcePack baseResourcePack = resourcePack.getResources(type, resourceId);

            if (baseResourcePack == null) {
                ZXLogger.warning("载入 " + ids + " 资源不存在");
                continue;
            }

            if (usedResources.contains(baseResourcePack)) {
                ZXLogger.warning("载入 " + ids + " 资源重复");
                continue;
            }

            //将启用的资源加入全局资源
            usedResources.add(baseResourcePack);
            ZXLogger.info("载入 " + ids);
        }

        GlobalResources.reloadResources(usedResources);
    }


}
