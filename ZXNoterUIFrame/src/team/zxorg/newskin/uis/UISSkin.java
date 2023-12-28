package team.zxorg.newskin.uis;

import team.zxorg.newskin.plist.PlistParser;
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
    private final HashMap<String, Path> imageMap = new HashMap<>();
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

    public int getAngle() {
        return angle;
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

        this.muiPath = muiPath;
        basePath = muiPath.getParent();

        try {
            parseMUI(muiPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        expressionCalculator.setUnitCanvasHeight(unit);
    }

    /**
     * 解析元件名
     *
     * @param line 元件名 如'customPrefix-[1-4]'
     * @return 包含的所有元件名
     */
    private static List<String> parseComponentNames(String line) {
        List<String> elementNames = new ArrayList<>();

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


        return "UISSkin{" +
                "calculator=" + expressionCalculator +
                ", imageMap=" + imageMap.size() +
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
                        imageMap.put(basePath.relativize(file).toString().replaceAll("\\\\", "/"), file);
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
     * 解析mui文件
     *
     * @param muiPath mui的路径
     */
    private void parseMUI(Path muiPath) throws IOException {
        Path muiDivPath = muiPath.getParent();
        enumerateImagesResource(muiDivPath);
        try (BufferedReader reader = Files.newBufferedReader(muiPath)) {
            String line;
            //当前作用的元件
            HashMap<String, UISComponent> currentComponents = new HashMap<>();
            //是属性值
            boolean isProperty;
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
                                    imageMap.put(cache.relativize(image).toString().replaceAll("\\\\", "/"), image);
                                }
                            } else {
                                ZXLogger.warning("引用 纹理包: " + texpackPath.getFileName() + " 文件不存在");
                            }
                        }
                        case "@include" -> {

                            String mui = null;
                            if (args.length == 3) {
                                double proportion = expressionCalculator.getCanvasWidth() / expressionCalculator.getCanvasHeight();
                                if (Math.abs(proportion - 1.7777) < 0.01) {
                                    if (args[1].contains("windows") || args[1].contains("mac")) {
                                        mui = args[2];
                                    }
                                } else if (args[1].contains("android") || args[1].contains("ios") || args[1].contains("touch")) {
                                    mui = args[2];
                                } else {
                                    boolean more = args[1].contains(">");
                                    try {
                                        double v = Double.parseDouble(args[1].replaceAll("[<>]", ""));
                                        //检查是否满足条件
                                        if (more && v < proportion || !more && v > proportion) {
                                            mui = args[2];
                                        }
                                    }catch (NumberFormatException e){

                                    }
                                }

                            } else if (args.length == 2) {
                                mui = args[1];
                            }

                            if (mui != null) {
                                //插入
                                Path includeMuiPath = muiPath.getParent().resolve(mui);
                                if (Files.exists(includeMuiPath)) {
                                    ZXLogger.info("引用 mui: " + includeMuiPath.getFileName());
                                    parseMUI(includeMuiPath);
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
                    }
                    continue;
                }
                if (isProperty) {
                    //是属性 为当前元素解析
                    int delimiter = line.indexOf("=");
                    String propertyName = line.substring(0, delimiter);
                    String value = line.substring(delimiter + 1);

                    //为当前组件设置属性
                    for (UISComponent property : currentComponents.values()) {
                        property.put(propertyName, value);
                    }

                } else {
                    //是元件 更新元件
                    currentComponents.clear();
                    //更新当前原件
                    for (String componentName : parseComponentNames(line)) {
                        if (componentMap.containsKey(componentName)) {
                            currentComponents.put(componentName, componentMap.get(componentName));
                        } else {
                            UISComponent newComponent = new UISComponent(componentName, imageMap, this);
                            currentComponents.put(componentName, newComponent);
                            componentMap.put(componentName, newComponent);
                        }
                    }
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
        Path file = imageMap.get(path.replaceAll("\\\\", "/"));
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


    /**
     * 获取被排序过的所有组件
     *
     * @return 所有组件
     */
    public List<UISComponent> getLinkedComponents() {
        List<UISComponent> sortedComponents = new ArrayList<>(componentMap.values());
        sortedComponents.sort(Comparator.comparingInt(component -> component.getInt("zindex", 0)));
        return sortedComponents;
    }
}
