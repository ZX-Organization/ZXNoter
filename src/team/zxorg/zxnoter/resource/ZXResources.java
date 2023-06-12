package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ZXResources {
    public static void loadResourcePackage(Path resourcePackagePath) {
        try {
            Stream<Path> pathStream = Files.walk(resourcePackagePath);
            //枚举所有资源文件
            pathStream.forEach(path -> {
                try {
                    if (!Files.isDirectory(path)) {
                        String type = path.getFileName().toString();
                        //获取资源类型
                        type = type.substring(type.lastIndexOf(".") + 1).toLowerCase();
                        String key = path.subpath(resourcePackagePath.getNameCount(), path.getNameCount()).toString();
                        //计算资源路径
                        key = key.substring(0, key.lastIndexOf(".")).replaceAll("[\\\\/]", ".").toLowerCase();
                        if (type.equals("png") || type.equals("jpg")) {//载入图片
                            Image image = new Image(path.toRealPath().toUri().toURL().toString());
                            allThings.put(key, image);
                        } else if (type.equals("json")) {//载入json
                            JSONObject json = JSONObject.parse(Files.readString(path));
                            String jsonType = json.getString("type");
                            if (jsonType.equals("language")) {//语言
                                String languageCode = json.getString("languageCode");
                                JSONObject languagesJSON = json.getJSONObject("languages");
                                Set<Map.Entry<String, Object>> languageSet = languagesJSON.entrySet();
                                Iterator<Map.Entry<String, Object>> iterator = languageSet.iterator();
                                while (iterator.hasNext()) {
                                    Map.Entry<String, Object> entry = iterator.next();
                                    allThings.put("lang." + languageCode + "." + entry.getKey(), entry.getValue());
                                }
                            } else if (jsonType.equals("guide")) {

                            }
                        } else if (type.equals("svg")) {//载入矢量图
                            SVGPath svg = new SVGPath();
                            svg.setContent(Utils.readSvg(path));
                            allThings.put(key, svg);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("载入资源 " + path + " 时发生异常: " + e);
                }
            });
            pathStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Object> allThings = new HashMap<>();//所有资源


    public static String getLanguageContent(String key, String code) {
        if (allThings.get("lang." + code + "." + key) instanceof String string)
            return string;
        else
            return getLanguageContent("language.loss", code);
    }

    /**
     * 获取矢量图图标
     *
     * @param key
     * @return
     */
    public static Pane getSvgPane(String key) {
        Pane iconPane = new Pane();
        if (allThings.get(key) instanceof Shape shape)
            iconPane.setShape(shape);
        else
            iconPane = getSvgPane("svg.icons.system.question-line");
        return iconPane;
    }

    /**
     * 获取图片资源
     *
     * @param key
     * @return
     */
    public static Image getImage(String key) {
        if (allThings.get(key) instanceof Image image)
            return image;
        else
            return getImage("img.unknown");
    }
}
