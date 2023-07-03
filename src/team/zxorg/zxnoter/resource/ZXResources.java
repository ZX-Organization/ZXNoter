package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.resource.pack.EditorResourcePack;
import team.zxorg.zxnoter.resource.pack.IconResourcePack;
import team.zxorg.zxnoter.resource.pack.StyleResourcePack;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class ZXResources {
    private static final JSONObject config = ZXConfig.getScopeConfig("resources");
    private static final HashMap<String, ResourcePack> loadedResourcePackMap = new HashMap<>();

    static {
        System.out.println("初始化资源");
        Path resourcesDiv = Path.of("resources");//资源目录
        try (Stream<Path> packPaths = Files.list(resourcesDiv)) {
            Path packPath;//资源包根路径
            Iterator<Path> iterator = packPaths.iterator();//资源包迭代器
            while (iterator.hasNext()) {//枚举所有资源包
                packPath = iterator.next();// 处理文件或子目录
                if (Files.exists(packPath.resolve("resource-pack.json"))) {//如果资源包有引导文件
                    ResourcePack resourcePack = new ResourcePack(packPath);
                    if (loadedResourcePackMap.get(resourcePack.id) == null) {//检查资源id是否重复
                        loadedResourcePackMap.put(resourcePack.id, resourcePack);
                    } else {
                        System.err.println("资源包id冲突:" + resourcePack.id + " - " + resourcePack.packPath);
                    }
                } else {
                    System.err.println("资源包缺失引导");
                }
            }
        } catch (IOException e) {
            System.err.println("资源包枚举中断");
            e.printStackTrace();
        }
    }

}
