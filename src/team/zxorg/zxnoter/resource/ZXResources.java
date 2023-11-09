package team.zxorg.zxnoter.resource;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.config.ZXConfig;
import team.zxorg.zxnoter.resource.config.ZXDefaultConfig;
import team.zxorg.zxnoter.resource.pack.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class ZXResources {
    /**
     * 载入的资源包 资源包id-资源包
     */
    public static final ObservableMap<String, ResourcePack> loadedResourcePackMap = FXCollections.observableMap(new HashMap<>());

    /**
     * 使用的附加资源
     */
    public static final HashMap<ResourceType, ObservableList<BaseResourcePack>> usedAttachResourcesMap = new HashMap<>();

    /**
     * 基础资源
     */
    public static final HashMap<ResourceType, ObjectProperty<BaseResourcePack>> usedBaseResourcesMap = new HashMap<>();

    /**
     * 使用的所有资源
     */
    public static final HashMap<ResourceType, ObservableList<BaseResourcePack>> usedAllResourcesMap = new HashMap<>();

    /**
     * 使用的所有资源
     */
    public static final ObservableList<BaseResourcePack> usedAllResources = FXCollections.observableArrayList(new ArrayList<>());

    public static ObjectProperty<BaseResourcePack> getUsedBaseResources(ResourceType resourceType) {
        return usedBaseResourcesMap.get(resourceType);
    }

    public static ObservableList<BaseResourcePack> getUsedAttachResources(ResourceType resourceType) {
        return usedAttachResourcesMap.get(resourceType);
    }

    public static ObservableList<BaseResourcePack> getUsedAllResources(ResourceType resourceType) {
        return usedAllResourcesMap.get(resourceType);
    }


    public static final Image LOGO;
    public static final Image LOGO_X26;
    public static final Image UNKNOWN;
    public static final Image DEFAULT_RESOURCE_ICON;
    public static final Image DEFAULT_RESOURCE_PACK_ICON;

    static {
        ZXLogger.info("载入基本资源");
        try (InputStream logoStream = ZXResources.class.getResourceAsStream("/base/logo.png");
             InputStream logoX26Stream = ZXResources.class.getResourceAsStream("/base/logo-x26.png");
             InputStream unknownStream = ZXResources.class.getResourceAsStream("/base/unknown.png");
             InputStream defaultResourceIconStream = ZXResources.class.getResourceAsStream("/base/default-resource-icon.png");
             InputStream defaultResourcePackIconStream = ZXResources.class.getResourceAsStream("/base/default-resource-pack-icon.png")) {

            LOGO = new Image(logoStream);
            LOGO_X26 = new Image(logoX26Stream);
            UNKNOWN = new Image(unknownStream);
            DEFAULT_RESOURCE_ICON = new Image(defaultResourceIconStream);
            DEFAULT_RESOURCE_PACK_ICON = new Image(defaultResourcePackIconStream);

        } catch (IOException e) {
            ZXLogger.severe("载入基本资源发生异常");
            throw new RuntimeException(e);
        }
        //初始化资源
        for (ResourceType resourceType : ResourceType.values()) {
            ObservableList<BaseResourcePack> usedAllResourcesType = FXCollections.observableArrayList(new ArrayList<>());
            usedAllResourcesMap.put(resourceType, usedAllResourcesType);

            SimpleObjectProperty<BaseResourcePack> simpleObjectProperty = new SimpleObjectProperty<>();
            usedBaseResourcesMap.put(resourceType, simpleObjectProperty);
            simpleObjectProperty.addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    usedAllResourcesType.remove(oldValue);
                    usedAllResources.remove(oldValue);
                }
                usedAllResourcesType.add(newValue);
                usedAllResources.add(newValue);

            });

            ObservableList<BaseResourcePack> usedAttachResourcesType = FXCollections.observableArrayList(new ArrayList<>());
            usedAttachResourcesMap.put(resourceType, usedAttachResourcesType);
            usedAttachResourcesType.addListener((ListChangeListener<BaseResourcePack>) c -> {
                while (c.next()) {
                    if (c.wasAdded()) {
                        usedAllResources.addAll(c.getAddedSubList());
                        usedAllResourcesType.addAll(c.getAddedSubList());
                    } else if (c.wasRemoved()) {
                        usedAllResources.removeAll(c.getRemoved());
                        usedAllResourcesType.removeAll(c.getRemoved());
                    }
                }
            });


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
     * 搜索内部资源包目录
     */
    public static void loadInternalPacks() {
        Path resourcePath = Paths.get("res/resources/base-pack");
        if (!Files.exists(resourcePath)) {
            try {
                URL resourceUrl = ZXConfig.class.getResource("/resources/base-pack");
                ZXLogger.info("载入内部资源 url:" + resourceUrl);
                URI resourceUri = resourceUrl.toURI();
                Map<String, String> env = new HashMap<>();
                env.put("create", "true");
                FileSystem zipfs = FileSystems.newFileSystem(resourceUri, env);
                resourcePath = zipfs.getPath("/resources/base-pack");
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
                ZXLogger.severe("载入内部资源异常");
            }
        }
        ZXResources.loadPack(resourcePath);
    }

    public static void loadZipPack(Path resourcePath) {
        try {
            URI resourceUri = URI.create("jar:" + resourcePath.normalize().toUri());
            ZXLogger.info("载入Zip资源 " + resourceUri);
            Map<String, String> env = new HashMap<>();
            env.put("create", "true");
            FileSystem zipfs = FileSystems.newFileSystem(resourceUri, env);
            resourcePath = zipfs.getPath("/");
            ZXResources.loadPack(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
            ZXLogger.severe("载入Zip资源异常");
        }
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
                Path resourcePack = iterator.next();
                if (Files.isDirectory(resourcePack)) {
                    loadPack(resourcePack);
                } else {
                    if (resourcePack.toString().toLowerCase().endsWith(".zip")) {
                        loadZipPack(resourcePack);
                    } else if (resourcePack.toString().toLowerCase().endsWith(".zxrp")) {
                        loadZipPack(resourcePack);
                    }
                }
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

    public static ResourcePack getResourcePackFromId(String packId) {
        return loadedResourcePackMap.get(packId);
    }

    public static BaseResourcePack getResourceFromFullId(String ids) {
        String packId;
        ResourceType type;
        String resourceId;
        {
            String[] id = ids.split("\\.");
            if (id.length != 3) {
                ZXLogger.warning("载入 " + ids + " id分隔异常");
                throw new IllegalArgumentException("id分隔异常 " + ids);
            }

            packId = id[0];
            type = ResourceType.valueOf(id[1]);
            resourceId = id[2];
        }
        return getResourcePackFromId(packId).getResources(type, resourceId);
    }

    /**
     * 重载全局资源 (读取配置文件为资源包索引资源)
     */
    public static void reloadGlobalResource(ResourceType resourceType) {
        ZXLogger.info("载入引用的资源包列表 " + resourceType);
        //清除其他资源
        usedAttachResourcesMap.get(resourceType).clear();


        /*{
            BaseResourcePack baseResourcePack = getResourceFromFullId();
            //将启用的资源加入全局资源
            usedBaseResourcesMap.get(resourceType).set(baseResourcePack);
            ZXLogger.info("载入基础 " + baseResourcePack);
        }*/
        System.out.println(resourceType);
        System.out.println(ZXDefaultConfig.configuration.resourcePacks.getTypeResourcePacks(resourceType));
        for (String fullIds : ZXDefaultConfig.configuration.resourcePacks.getTypeResourcePacks(resourceType)) {
            BaseResourcePack baseResourcePack = getResourceFromFullId(fullIds);
            //将启用的资源加入全局资源
            usedBaseResourcesMap.get(resourceType).set(baseResourcePack);
            ZXLogger.info("载入基础资源包 " + baseResourcePack);
        }

        /*for (String fullIds : UserPreferenceOld.getUsedAttachResources(resourceType)) {
            BaseResourcePack baseResourcePack = getResourceFromFullId(UserPreferenceOld.getUsedBaseResources(resourceType));
            //将启用的资源加入全局资源
            usedBaseResourcesMap.get(resourceType).set(baseResourcePack);
            ZXLogger.info("载入附加 " + baseResourcePack);
        }*/
        GlobalResources.loadResourcesPacks(resourceType);
    }


}
