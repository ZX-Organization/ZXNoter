package team.zxorg.skin.uis;

import team.zxorg.skin.DeviceType;
import team.zxorg.skin.plist.PlistParser;
import team.zxorg.skin.uis.component.AbstractComponentRenderer;
import team.zxorg.ui.component.LayerCanvasPane;
import team.zxorg.zxncore.ZXLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UISSkin {
    /**
     * 变量映射表 引用会是'{test}'
     * <p>
     * 默认包含以下值
     * {title}: 表示歌曲名
     * {artist}: 表示歌曲艺术家
     * {creator}: 表示谱面作者名
     * {version}: 谱面难度名
     */
    protected final HashMap<String, String> variable = new HashMap<>();
    private final ExpressionCalculator expressionCalculator;
    /**
     * 资源映射表
     */
    private final HashMap<String, Path> imagePathMap = new HashMap<>();

    /**
     * 组件表
     */
    private final HashMap<String, UISComponent> componentMap = new HashMap<>();
    /**
     * 基本路径
     */
    private final Path basePath;
    /**
     * 主mui路径
     */
    private final Path muiPath;
    /**
     * 3d角度
     */
    int angle = 0;
    /**
     * 单位 默认720
     */
    int unit = 720;

    DeviceType deviceType = DeviceType.WINDOWS;

    public int getAngle() {
        return angle;
    }

    /**
     * 设置设备类型 会影响到include判断
     *
     * @param deviceType 设备类型
     */
    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 通过mui文件 解析到皮肤
     *
     * @param muiPath mui文件
     */
    public UISSkin(Path muiPath, ExpressionCalculator expressionCalculator) {
        this.expressionCalculator = expressionCalculator;
        variable.put("title", "{ 歌曲名 }");
        variable.put("artist", "{ 歌曲艺术家 }");
        variable.put("creator", "{ 谱面作者名 }");
        variable.put("version", "{ 谱面难度名 }");
        variable.put("player", "{ 玩家ID }");
        variable.put("bpm", "{ BPM }");

        this.muiPath = muiPath;
        basePath = muiPath.getParent();
    }

    public void parse() {
        try {
            parseMUI(muiPath, componentMap);
            expressionCalculator.setUnitCanvasHeight(unit);
        } catch (IOException e) {
            ZXLogger.warning("解析mui文件失败: " + e.getMessage());
        }
    }

    /**
     * 解析元件名
     *
     * @param line 元件名 如'customPrefix-[1-4]'
     * @return 包含的所有元件名
     */
    private static List<String> parseComponentNames(String line) {

        List<String> elementNames = new ArrayList<>();
        if (!line.contains("[") || !line.contains("]")) {
            elementNames.add(line.trim());
            return elementNames;
        }
        Pattern pattern = Pattern.compile("([a-zA-Z_\\d]+)-?\\[([^\\]]+)\\]|([a-zA-Z_\\d]+)-?([a-zA-Z_\\d]*)");
        Matcher matcher = pattern.matcher(line.trim());

        if (matcher.find()) {
            String prefix = matcher.group(1) != null ? matcher.group(1) : matcher.group(3);

            if (matcher.group(2) != null) {
                String numbers = matcher.group(2);

                if (numbers.contains("-")) {
                    String[] range = numbers.split("-");
                    int start = Integer.parseInt(range[0].trim());
                    int end = Integer.parseInt(range[1].trim());

                    for (int i = start; i <= end; i++) {
                        elementNames.add(prefix + "-" + i);
                    }
                } else {
                    String[] numberArray = numbers.split(",");

                    for (String number : numberArray) {
                        elementNames.add(prefix + "-" + number.trim());
                    }
                }
            } else if (matcher.group(4) != null) {
                // 支持 customPrefix-1 这种情况
                if (matcher.group(4).isEmpty()) {
                    elementNames.add(prefix);
                } else {
                    elementNames.add(prefix + "-" + matcher.group(4));
                }
            }
        }

        return elementNames;
    }

    /**
     * 判断文件是否为图片类型（png, jpg, bmp）
     *
     * @param file 文件
     * @return 是否是图片
     */
    private static boolean isImageFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".bmp");
    }

    @Override
    public String toString() {


        return "UISSkin {" +
                "calculator=" + expressionCalculator +
                ", imageMap=" + imagePathMap.size() +
                ", componentMap=" + componentMap.size() +
                ", angle=" + angle +
                ", unit=" + unit +
                ", variable=" + variable +
                ", basePath=" + basePath +
                '}';
    }

    /**
     * 获取表达式计算器
     *
     * @return 表达式计算器
     */
    public ExpressionCalculator getExpressionCalculator() {
        return expressionCalculator;
    }

    /**
     * 获取主MUI路径
     *
     * @return MUI路径
     */
    public Path getMuiPath() {
        return muiPath;
    }

    /**
     * 粗略的枚举资源文件 注: 忽略'cache'文件目录
     *
     * @param basePath 基本路径
     */
    private void enumerateImagesResource(Path basePath) {

        try {
            Files.walkFileTree(basePath, new FileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (dir.getFileName().toString().equals("cache"))
                        return FileVisitResult.SKIP_SUBTREE;
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    // 判断文件类型是否为图片类型
                    if (isImageFile(file)) {
                        imagePathMap.put(basePath.relativize(file).toString().replaceAll("\\\\", "/"), file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新渲染器
     */
    public boolean updateRenderer(List<AbstractComponentRenderer> renderers, HashMap<UISComponent, AbstractComponentRenderer> currentComponentMap, LayerCanvasPane layerCanvasPane) {
        angle = 0;
        HashMap<String, UISComponent> newComponentMap = new HashMap<>();
        boolean isChanged = false;
        try {
            parseMUI(muiPath, newComponentMap);
            expressionCalculator.setUnitCanvasHeight(unit);
        } catch (IOException e) {
            ZXLogger.warning("解析mui文件失败: " + e.getMessage());
            return isChanged;
        }

        //检查多余的组件
        List<String> needRemoval = new ArrayList<>();
        for (String name : componentMap.keySet()) {
            if (!newComponentMap.containsKey(name)) {
                needRemoval.add(name);
                System.out.println("移除组件：" + name);
            }
        }
        //移除多余组件
        for (String name : needRemoval) {
            UISComponent component = componentMap.get(name);
            AbstractComponentRenderer renderer = currentComponentMap.get(component);
            renderers.remove(renderer);
            currentComponentMap.remove(component);
            componentMap.remove(name);
            if (component.isAnimation())
                isChanged = true;
        }

        //检查新组件
        for (UISComponent component : newComponentMap.values()) {
            if (!componentMap.containsKey(component.getFullName())) {
                AbstractComponentRenderer renderer = AbstractComponentRenderer.toRenderer(component, layerCanvasPane);
                if (renderer == null) {
                    ZXLogger.warning("不支持的组件: " + component.getFullName() + "   " + component);
                    continue;
                }
                renderers.add(renderer);
                currentComponentMap.put(component, renderer);
                componentMap.put(component.getFullName(), component);
                if (component.isAnimation())
                    isChanged = true;
            }
        }

        //更新其余组件属性
        for (UISComponent component : componentMap.values()) {
            UISComponent newComponent = newComponentMap.get(component.getFullName());
            if (!component.equals(newComponent)) {
                component.copyFrom(newComponent);
                if (component.isAnimation())
                    isChanged = true;
            }
        }

        //再次排序渲染器
        sortRenders(renderers);
        return isChanged;
    }


    /**
     * 排序渲染器 按照zindex排序
     *
     * @param renderers 渲染器
     */
    public static void sortRenders(List<AbstractComponentRenderer> renderers) {
        renderers.sort(Comparator.comparingInt(AbstractComponentRenderer::getZindex));
    }

    /**
     * 条件判断
     *
     * @param condition 条件  可以判断设备和宽高比
     * @return 是否符合条件
     */
    private boolean conditionalJudgment(String condition) {
        double proportion = expressionCalculator.getCanvasWidth() / expressionCalculator.getCanvasHeight();
        if (condition.equals("true"))
            return true;
        if (condition.equalsIgnoreCase(deviceType.name())) {
            return true;
        } else {
            //是否是小于
            boolean isLess = condition.contains("<");
            try {
                double v = Double.parseDouble(condition.replaceAll("[<>]", ""));
                //检查是否满足条件
                if (isLess && proportion < v || !isLess && proportion > v) {
                    return true;
                }
            } catch (NumberFormatException e) {

            }
        }
        return false;
    }

    /**
     * 解析mui文件
     *
     * @param muiPath mui的路径
     */
    private void parseMUI(Path muiPath, HashMap<String, UISComponent> componentMap) throws IOException {
        Path muiDivPath = muiPath.getParent();
        enumerateImagesResource(muiDivPath);
        try (BufferedReader reader = Files.newBufferedReader(muiPath)) {
            String line;
            //当前作用的元件
            List<UISComponent> currentComponents = new ArrayList<>();
            //是属性值
            boolean isProperty;
            //跳过  (条件判断)
            boolean skip = false;
            //是动画
            boolean isAnimation = false;
            //解析mui
            while ((line = reader.readLine()) != null) {
                //判断是否是属性
                isProperty = line.startsWith("\t") || line.startsWith("  ");
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    // 跳过空行和注释
                    continue;
                } else if (line.startsWith("@")) {
                    //解析命令
                    String[] args = line.split(" ");
                    switch (args[0]) {
                        case "@texpack" -> {


                            //纹理包
                            Path texpackPath = muiDivPath.resolve(args[1] + ".plist");
                            if (Files.exists(texpackPath)) {
                                ZXLogger.info("引用 纹理包: " + args[1]);
                                Path cache = muiDivPath.resolve("cache");
                                if (!Files.exists(cache))
                                    Files.createDirectories(cache);
                                List<Path> images = PlistParser.parser(texpackPath, cache, true);
                                //加入到图片索引里
                                for (Path image : images) {
                                    imagePathMap.put(cache.relativize(image).toString().replaceAll("\\\\", "/"), image);
                                }
                            } else {
                                ZXLogger.warning("引用 纹理包: " + texpackPath.getFileName() + " 文件不存在");
                            }
                        }
                        case "@include" -> {

                            String mui = null;
                            if (args.length == 3) {
                                if (conditionalJudgment(args[1])) {
                                    mui = args[2];
                                }
                            } else if (args.length == 2) {
                                mui = args[1];
                            }
                            if (mui != null) {
                                //插入
                                Path includeMuiPath = muiPath.getParent().resolve(mui);
                                if (Files.exists(includeMuiPath)) {
                                    ZXLogger.info("引用 mui: " + includeMuiPath.getFileName());
                                    parseMUI(includeMuiPath, componentMap);
                                } else {
                                    ZXLogger.warning("引用 mui: " + includeMuiPath.getFileName() + " 文件不存在");
                                }
                            }

                        }
                        case "@angle" -> angle = Integer.parseInt(args[1]);//角度
                        case "@unit" -> {
                            unit = Integer.parseInt(args[1]);
                            expressionCalculator.setUnitCanvasHeight(unit);
                        }//单位像素
                        case "@define" -> variable.put(args[1], args[2]);//变量
                        case "@if" -> {
                            if (conditionalJudgment(args[1])) {
                                skip = false;
                            } else {
                                skip = true;
                            }
                        }
                        case "@endif" -> skip = false;
                    }
                    continue;
                }
                if (skip)
                    continue;
                if (isProperty) {
                    //是属性 为当前元素解析
                    int delimiter = line.indexOf("=");
                    String propertyName = line.substring(0, delimiter);
                    String value = line.substring(delimiter + 1);

                    if (isAnimation) {
                        for (UISComponent property : currentComponents) {
                            property.putAnimation(value);
                        }
                    } else {

                        boolean isEmbeddedAnimation = propertyName.equals("motion") && (value.startsWith("!name=") || value.startsWith(":name="));
                        String componentName = "ea_" + value;
                        //为当前组件设置属性
                        for (int i = 0; i < currentComponents.size(); i++) {
                            if (isEmbeddedAnimation) {
                                currentComponents.get(i).putProperty(propertyName, componentName, i);
                            } else {
                                currentComponents.get(i).putProperty(propertyName, value, i);
                            }
                        }
                        //检查是否是内嵌动画
                        if (isEmbeddedAnimation) {
                            UISComponent newComponent = new UISComponent(":" + componentName, imagePathMap, this);
                            newComponent.putAnimation(value.substring(6));
                            componentMap.put(":" + componentName, newComponent);
                            System.out.println(newComponent);
                        }
                    }


                } else {
                    //是元件 更新元件
                    currentComponents.clear();
                    //更新当前原件
                    for (String componentName : parseComponentNames(line)) {
                        if (componentMap.containsKey(componentName)) {
                            currentComponents.add(componentMap.get(componentName));
                        } else {
                            UISComponent newComponent = new UISComponent(componentName, imagePathMap, this);
                            currentComponents.add(newComponent);
                            componentMap.put(componentName, newComponent);
                        }
                    }
                    isAnimation = line.startsWith(":");
                }

            }
        }
    }

    /**
     * 获取资源文件
     *
     * @param path 资源路径
     * @return 资源数据流
     */
    public InputStream getResource(String path) {
        Path file = imagePathMap.get(path.replaceAll("\\\\", "/"));
        if (Files.exists(file)) {
            try {
                return Files.newInputStream(file);
            } catch (IOException ignored) {
                ZXLogger.warning("打开 uis 文件时出错: " + file.getFileName());
            }
        }
        ZXLogger.warning("找不到 uis 文件: " + file.getFileName());
        return null;
    }

    /**
     * 获取组件
     *
     * @param name 组件名
     * @return 组件
     */
    public UISComponent getComponent(String name) {
        return componentMap.get(name);
    }

    /**
     * 获取所有组件
     *
     * @return 所有组件
     */
    public Collection<UISComponent> getComponents() {
        return componentMap.values();
    }


}
