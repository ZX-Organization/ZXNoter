package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
                        String name = path.getFileName().toString();
                        String type = name;
                        //获取资源名
                        name = name.substring(0, name.lastIndexOf(".")).toLowerCase();
                        //获取资源类型
                        type = type.substring(name.length() + 1).toLowerCase();
                        String key = path.subpath(resourcePackagePath.getNameCount(), path.getNameCount()).toString();
                        //计算资源路径
                        key = key.substring(0, key.lastIndexOf(".")).replaceAll("[\\\\/]", ".").toLowerCase();

                        switch (type) {
                            case "png", "jpg", "gif" -> { //载入图片
                                Image image = new Image(path.toRealPath().toUri().toURL().toString());
                                allThings.put(key, image);
                            }
                            case "json" -> { //载入json
                                JSONObject json = JSONObject.parse(Files.readString(path));
                                String jsonType = json.getString("type");
                                if (jsonType.equals("language")) {//语言
                                    String languageCode = json.getString("languageCode");
                                    JSONObject languagesJSON = json.getJSONObject("languages");
                                    Set<Map.Entry<String, Object>> languageSet = languagesJSON.entrySet();
                                    for (Map.Entry<String, Object> entry : languageSet) {
                                        allThings.put("lang." + languageCode + "." + entry.getKey(), entry.getValue());
                                    }
                                } else if (jsonType.equals("guide")) {

                                }
                            }
                            case "svg" -> { //载入矢量图
                                SVGPath svg = new SVGPath();
                                svg.setContent(Utils.readSvg(path));
                                allThings.put(key, svg);
                            }
                            case "css" -> { //载入样式表
                                if (key.startsWith("css.theme")) {//主题样式
                                    themes.add(key);
                                }
                                allThings.put(key, path.toRealPath());
                            }
                            default -> allThings.put(key, path);
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

    /**
     * 所有资源 记录万物
     */
    public static HashMap<String, Object> allThings = new HashMap<>();
    /**
     * 主题列表
     */
    public static ArrayList<String> themes = new ArrayList<>();

    public static String getLanguageContent(String key, String code) {
        if (getObject("lang." + code + "." + key) instanceof String string)
            return string;
        else
            return getLanguageContent("language.loss", code);
    }


    public static Path getPath(String key) {
        if (getObject(key) instanceof Path path) {
            if (Files.exists(path))
                return path;
            throw new RuntimeException("引用的文件不存在。");
        } else
            throw new RuntimeException("引用未知文件。");
    }

    public static Object getObject(String key) {
        return allThings.get(key.toLowerCase());
    }


    public static Shape getSvg(String key) {
        if (getObject(key) instanceof Shape shape)
            return shape;
        else
            return getSvg("svg.icons.system.question-line");
    }


    /**
     * 获取矢量图图标
     */
    public static Pane getSvgPane(String key) {
        Pane iconPane = new Pane();
        iconPane.setShape(getSvg(key));
        return iconPane;
    }


    public static Pane getSvgPane(String key, double size, Color color) {
        Pane svg = getSvgPane(key);
        svg.setPrefSize(size, size);
        svg.setBackground(Background.fill(color));
        return svg;
    }

    /**
     * 获取图片资源
     */
    public static Image getImage(String key) {
        if (allThings.get(key) instanceof Image image)
            return image;
        else
            return getImage("img.unknown");
    }

    public static void main(String[] args) {
        /*System.out.println(ZXResources.getLanguageContent("languageCode.zh_cn","zh_cn"));
        System.out.println(ZXResources.getLanguageContent("titleBar.menu.file","zh_cn"));
        System.out.println(ZXResources.getLanguageContent("cnmd","zh_cn"));

        System.out.println(ZXResources.getLanguageContent("languageCode.en_us","en_us"));
        System.out.println(ZXResources.getLanguageContent("titleBar.menu.file","en_us"));
        System.out.println(ZXResources.getLanguageContent("cnmd","en_us"));*/


        /*Pane icon = ZXResources.getSvgPane("a");
        icon.setPrefSize(22, 22);
        icon.setBackground(Background.fill(Color.YELLOW));
        HBox.setMargin(icon, new Insets(4));

        Pane icon2 = ZXResources.getSvgPane("svg.icons.zxnoter.zxnoter");
        icon2.setPrefSize(22, 22);
        icon2.setBackground(Background.fill(Color.YELLOW));
        HBox.setMargin(icon2, new Insets(4));*/


        /*Image image=ZXResources.getImage("sdas");
        Image image2=ZXResources.getImage("img.zxnoter.zxnoter");*/
    }
}
