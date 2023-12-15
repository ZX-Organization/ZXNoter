package team.zxorg.skin.uis;

import javafx.geometry.Pos;
import team.zxorg.skin.basis.ElementRender;
import team.zxorg.skin.components.*;
import team.zxorg.zxncore.ZXLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UISParser {

    public static HashMap<String, HashMap<String, String>> parseToMap(Path file) throws IOException {
        return parseToMap(file, null);
    }

    public static HashMap<String, HashMap<String, String>> parseToMapStr(Path divPath, String uis) throws IOException {
        return parseToMap(divPath, new BufferedReader(new StringReader(uis)), null);
    }

    private static HashMap<String, HashMap<String, String>> parseToMap(Path file, HashMap<String, HashMap<String, String>> uis) throws IOException {
        System.out.println("uis 开始解析: " + file.getFileName());
        uis = parseToMap(file.getParent(), Files.newBufferedReader(file), uis);
        System.out.println("uis 解析完成: " + file.getFileName());
        return uis;
    }

    private static HashMap<String, HashMap<String, String>> parseToMap(Path divPath, BufferedReader reader, HashMap<String, HashMap<String, String>> uis) throws IOException {
        if (uis == null) uis = new HashMap<>();

        String line;
        HashMap<String, HashMap<String, String>> currentElements = new HashMap<>();
        boolean isProperty;

        while ((line = reader.readLine()) != null) {
            //判断是否是属性
            isProperty = line.startsWith("\t") || line.startsWith("  ");

            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                // 跳过空行和注释
                continue;
            } else if (line.startsWith("@")) {
                //命令
                String[] args = line.split(" ");
                switch (args[0]) {
                    case "@include" -> {
                        Path includePath = divPath.resolve(args[1]);
                        if (Files.exists(includePath)) {
                            System.out.println("uis 插入: " + includePath.getFileName());
                            parseToMap(includePath, uis);
                        } else {
                            ZXLogger.warning("uis 插入: " + includePath.getFileName() + " 文件不存在！");
                        }
                    }
                    case "@angle" -> {
                        HashMap<String, String> angle = uis.computeIfAbsent("parameter", k -> new HashMap());
                        angle.put("angle", args[1]);
                    }
                }
                continue;
            }
            if (isProperty) {
                //是属性 为当前元素解析
                int delimiter = line.indexOf("=");
                String propertyName = line.substring(0, delimiter);
                String value = line.substring(delimiter + 1);
                for (HashMap<String, String> property : currentElements.values()) {
                    property.put(propertyName, value);
                }

            } else {
                //是元素 更新元素
                //System.out.println("Element: " + line);
                currentElements.clear();
                for (String elementName : parseElementNames(line)) {
                    if (uis.containsKey(elementName)) {
                        currentElements.put(elementName, uis.get(elementName));
                    } else {
                        HashMap<String, String> newElement = new HashMap<>();
                        currentElements.put(elementName, newElement);
                        uis.put(elementName, newElement);
                    }
                }
            }

        }
        return uis;
    }

    public static HashMap<String, ElementRender> parseToElementMap(Path file) {
        HashMap<String, HashMap<String, String>> uisMap;
        try {
            uisMap = parseToMap(file);
        } catch (IOException e) {
            ZXLogger.warning("Error parsing uis file: " + file.getFileName());
            throw new RuntimeException(e);
        }
        return parseToElementMap(file.getParent(), uisMap);
    }

    public static HashMap<String, ElementRender> parseToElementMap(Path divPath, HashMap<String, HashMap<String, String>> uisMap) {

        HashMap<String, ElementRender> uis = new HashMap<>();


        for (String elementName : uisMap.keySet()) {
            ElementRender element = null;
            HashMap<String, String> properties = uisMap.get(elementName);
            try {
                if (elementName.startsWith("note-")) {
                    element = new NoteComponent(properties, divPath);
                } else if (elementName.startsWith("press-")) {
                    element = new PressComponent(properties, divPath);
                } else if (elementName.startsWith("hit-")) {
                    element = new HitComponent(properties, divPath);
                } else if (elementName.startsWith("parameter")) {
                    element = new PerspectiveComponent(properties.getOrDefault("angle", "0"));
                } else if (elementName.equals("touch")) {
                    element = new TouchComponent(properties, divPath);
                } else if (elementName.startsWith("_")) {
                    switch (UISConventionalElementType.values()[Integer.parseInt(properties.getOrDefault("type", "0"))]) {
                        case ORDINARY -> {
                            System.out.println("解析组件: " + elementName);
                            element = new OrdinaryComponent(properties, divPath);
                        }
                    }
                }
            } catch (Exception e) {
                ZXLogger.warning(elementName + " 组件解析失败: " + e.getMessage());
            }

            if (element != null) uis.put(elementName, element);
        }

        return uis;
    }

    public static InputStream getResource(Path divPath, String path) {
        Path file = divPath.resolve(path);
        if (Files.exists(file)) {
            try {
                return Files.newInputStream(file);
            } catch (IOException ignored) {
                ZXLogger.warning("Error opening uis file: " + file.getFileName());
            }
        }
        ZXLogger.warning("Could not find uis file: " + file.getFileName());
        return null;
    }

    private static List<String> parseElementNames(String line) {
        List<String> elementNames = new ArrayList<>();

        Pattern pattern = Pattern.compile("([a-zA-Z_\\d]+)-?\\[([^\\]]+)\\]|([a-zA-Z_\\d]+)-?([a-zA-Z_\\d]*)");
        Matcher matcher = pattern.matcher(line);

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
     * 获取锚点位置 默认为 TOP_CENTER
     *
     * @param anchor 锚点参数
     * @return 锚点位置
     */
    public static Pos getAnchorPos(String anchor) {
        return switch (anchor) {
            case "1" -> Pos.TOP_CENTER;
            case "2" -> Pos.TOP_RIGHT;
            case "3" -> Pos.CENTER_LEFT;
            case "4" -> Pos.CENTER;
            case "5" -> Pos.CENTER_RIGHT;
            case "6" -> Pos.BOTTOM_LEFT;
            case "7" -> Pos.BOTTOM_CENTER;
            case "8" -> Pos.BOTTOM_RIGHT;
            default -> Pos.TOP_LEFT;
        };
    }
}

