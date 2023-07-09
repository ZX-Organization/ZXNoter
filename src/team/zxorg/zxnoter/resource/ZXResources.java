package team.zxorg.zxnoter.resource;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.type.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ZXResources {
    /**
     * 载入的资源包
     */
    private static final HashMap<String, ResourcePack> loadedResourcePackMap = new HashMap<>();

    /**
     * 全局资源
     */
    private static final HashMap<ResourceType, ArrayList<Resource>> globalResources = new HashMap<>();

    public static SVGPath getIconPane(String key) {
        SVGPath svgPath;
        for (Resource resource : globalResources.get(ResourceType.icon)) {
            if (resource instanceof IconResource iconResource) {
                svgPath = iconResource.getIcon(key);
                return svgPath;
            }
        }
        return null;
    }

    public static Pane getIconPane(String key, double size) {
        Pane pane = new Pane();
        SVGPath svgPath;
        for (Resource resource : globalResources.get(ResourceType.icon)) {
            if (resource instanceof IconResource iconResource) {
                svgPath = iconResource.getIcon(key);
                pane.setShape(svgPath);
                pane.getStyleClass().add("icon");
                pane.setPrefSize(size, size);
                pane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
                return pane;
            }
        }
        return null;
    }

    public static String getLanguageContent(String key) {
        String content;
        for (Resource resource : globalResources.get(ResourceType.language)) {
            if (resource instanceof LanguageResource languageResource) {
                content = languageResource.getLanguageContent(key);
                if (content != null)
                    return content;
            }
        }
        return null;
    }

    /**
     * 为Scene重新应用样式
     *
     * @param scene 被设置的Scene
     */
    public static void setSceneStyle(Scene scene) {
        ObservableList<String> lists = scene.getStylesheets();
        lists.clear();
        for (Resource resource : globalResources.get(ResourceType.layout)) {
            if (resource instanceof LayoutResource layoutResource) {
                Iterator<Path> iterator = layoutResource.iterator();
                while (iterator.hasNext()) {
                    lists.add(iterator.next().toUri().toString());
                }
            }
        }
        for (Resource resource : globalResources.get(ResourceType.color)) {
            if (resource instanceof ColorResource colorResource) {
                Iterator<Path> iterator = colorResource.iterator();
                while (iterator.hasNext()) {
                    lists.add(iterator.next().toUri().toString());
                }
            }
        }
    }

    public static Pane getIconPane(String key, double size, ZXColor color) {
        Pane pane = new Pane();
        pane.setShape(getIconPane(key));
        pane.setPrefSize(size, size);
        pane.getStyleClass().add("bg-" + color);
        return pane;
    }

    public static void clearPacks() {
        ZXLogger.logger.info("清除资源包");
        loadedResourcePackMap.clear();
    }

    /**
     * 重新搜索资源包
     */
    public static void searchPacks(Path resourcesDiv) {
        if (!Files.exists(resourcesDiv)) {
            ZXLogger.logger.warning("本地资源包目录不存在");
            return;
        }

        try (Stream<Path> packPaths = Files.list(resourcesDiv)) {
            Path packPath;//资源包根路径
            Iterator<Path> iterator = packPaths.iterator();//资源包迭代器
            while (iterator.hasNext()) {//枚举所有资源包
                packPath = iterator.next();// 处理文件或子目录
                if (Files.exists(packPath.resolve("resource-pack.json"))) {//如果资源包有引导文件
                    ResourcePack resourcePack = new ResourcePack(packPath);

                    ResourcePack repeatPack = loadedResourcePackMap.get(resourcePack.getId());
                    if (repeatPack == null) {//检查资源id是否重复
                        ZXLogger.logger.info("载入资源包:" + resourcePack.getId());
                        loadedResourcePackMap.put(resourcePack.getId(), resourcePack);
                    } else {
                        ZXLogger.logger.warning("资源包id冲突 现有:" + repeatPack + " 冲突:" + resourcePack);
                    }
                } else {
                    ZXLogger.logger.warning("资源包缺失引导: " + packPath);
                }
            }
        } catch (IOException e) {
            ZXLogger.logger.warning("资源包枚举中断");
        }
    }

    /**
     * 重载全局资源 (读取配置文件为资源包索引资源)
     */
    public static void reloadGlobalResource() {
        globalResources.clear();
        //初始化所有资源map
        for (ResourceType type : ResourceType.values()) {
            globalResources.put(type, new ArrayList<>());
        }

        for (String ids : UserPreference.getUsedResources()) {
            ZXLogger.logger.info("载入资源包:" + ids);
            String packId;
            ResourceType type;
            String resourceId;
            {
                String[] id = ids.split("\\.");
                if (id.length != 3) {
                    ZXLogger.logger.warning("重载语言资源时 id长度异常:" + ids);
                    return;
                }

                packId = id[0];
                type = ResourceType.valueOf(id[1]);
                resourceId = id[2];
            }


            ResourcePack resourcePack = loadedResourcePackMap.get(packId);
            if (resourcePack == null) {
                ZXLogger.logger.warning("重载资源时 id为" + packId + "的资源包不存在");
                return;
            }

            Resource resource = resourcePack.getResources(type, resourceId);

            if (resource == null) {
                ZXLogger.logger.warning("重载资源时 id为" + resourceId + "的资源不存在 资源包id为" + packId);
                return;
            }

            //将启用的资源加入全局资源
            globalResources.get(type).add(resource);
            ZXLogger.logger.info("资源包载入完成:" + ids);
        }
    }


}
