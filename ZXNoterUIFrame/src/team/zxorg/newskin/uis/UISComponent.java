package team.zxorg.newskin.uis;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import team.zxorg.zxncore.ZXLogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UISComponent {
    public static final Image UNKNOWN;

    static {
        ZXLogger.info("载入基本资源");
        try (InputStream unknownStream = UISComponent.class.getResourceAsStream("/base/unknown.png");) {
            assert unknownStream != null;
            UNKNOWN = new Image(unknownStream);
        } catch (IOException e) {
            ZXLogger.severe("载入基本资源发生异常");
            throw new RuntimeException(e);
        }
    }

    public final ExpressionCalculator expressionCalculator;
    public final UISSkin uisSkin;
    /**
     * 资源映射表
     */
    private final HashMap<String, Path> imageMap;
    private final String name;
    private final String fullName;
    private final HashMap<String, String> properties = new HashMap<>();
    private int index = 0;
    private boolean changed = false;


    public UISComponent(String fullName, HashMap<String, Path> imageMap, UISSkin uisSkin) {
        this.imageMap = imageMap;
        this.fullName = fullName;
        if (fullName.contains("-"))
            this.name = fullName.substring(0, fullName.indexOf("-"));
        else
            this.name = fullName;
        this.uisSkin = uisSkin;
        this.expressionCalculator = uisSkin.getExpressionCalculator();
        try {
            index = Integer.parseInt(fullName.substring(fullName.indexOf("-") + 1)) - 1;
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public String toString() {
        return "UISComponent{" +
                "name='" + name + '\'' +
                "fullName='" + fullName + '\'' +
                ", index=" + index +
                ", properties=" + properties +
                '}';
    }

    /**
     * 设置属性
     *
     * @param name  属性名
     * @param value 属性值 如果是null为移除属性
     */
    public void put(String name, String value) {
        if (value == null)
            properties.remove(name);
        else
            properties.put(name, value.trim());
        changed = true;
    }

    /**
     * 包含属性
     *
     * @param name 属性名
     * @return 是否包含
     */
    public boolean contains(String name) {
        return properties.containsKey(name);
    }

    /**
     * 如果属性被修改过，则会返回true并复位。
     *
     * @return 是否被修改过
     */
    public boolean isChanged() {
        if (changed) {
            changed = false;
            return true;
        }
        return false;
    }

    public String getString(String name, String defaultValue) {
        String value = properties.getOrDefault(name, defaultValue);
        if (value == null)
            return null;
        // 匹配{}差值变量并覆盖
        Pattern pattern = Pattern.compile("\\{([^{}]+)\\}");
        Matcher matcher = pattern.matcher(value);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String replacement = uisSkin.variable.getOrDefault(variableName, "(未定义的变量: " + variableName + ")");
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);

        return result.toString().isEmpty() ? value : result.toString();
    }

    public UISFrame getFrame(String name) {
        return new UISFrame(this, name);
    }

    /**
     * 获取整数
     *
     * @param name         属性名
     * @param defaultValue 属性值
     * @return 整数
     */
    public int getInt(String name, int defaultValue) {
        try {
            return Integer.parseInt(getString(name, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取方向
     * @param name 属性名
     * @return 方向
     */
    public Orientation getOrientation(String name) {
        return switch (getInt(name, -1)) {
            case 0 -> Orientation.HORIZONTAL;
            case 1 -> Orientation.VERTICAL;
            default -> null;
        };
    }

    /**
     * 获取双精度值
     *
     * @param name         属性名
     * @param defaultValue 默认值
     * @return 双精度值小数
     */
    public double getDouble(String name, double defaultValue) {
        try {
            return Double.parseDouble(getString(name, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取路径
     *
     * @param path 路径
     * @return 路径 可能有null
     */
    public Path getPath(String path) {
        return imageMap.get(path);
    }

    /**
     * 获取图片
     *
     * @param name 属性名
     * @return 图片 没找到返回UNKNOWN
     */
    public Image getImage(String name) {
        String str = getString(name, null);
        if (str == null) {
            ZXLogger.warning(getFullName() + " 没有属性 " + name);
            return UNKNOWN;
        }
        return getImageFromPath(str);
    }

    /**
     * 获取图片
     *
     * @param str 路径名
     * @return 图片 没找到返回UNKNOWN
     */
    public Image getImageFromPath(String str) {
        Path path = getPath(str);
        if (path == null) {
            ZXLogger.warning(getFullName() + " 未找到资源 " + str);
            return UNKNOWN;
        }
        try {
            return new Image(Files.newInputStream(path));
        } catch (IOException e) {
            ZXLogger.warning(getFullName() + " 无法载入图片 " + path);
        }
        return UNKNOWN;
    }

    /**
     * 获取锚点位置 默认为 TOP_LEFT
     *
     * @param name 属性名
     * @return 锚点位置
     */
    public Pos getAnchorPos(String name) {
        return switch (getInt(name, 0)) {
            case 1 -> Pos.TOP_CENTER;
            case 2 -> Pos.TOP_RIGHT;
            case 3 -> Pos.CENTER_LEFT;
            case 4 -> Pos.CENTER;
            case 5 -> Pos.CENTER_RIGHT;
            case 6 -> Pos.BOTTOM_LEFT;
            case 7 -> Pos.BOTTOM_CENTER;
            case 8 -> Pos.BOTTOM_RIGHT;
            default -> Pos.TOP_LEFT;
        };
    }

    /**
     * 获取图片序列
     *
     * @param name 属性名
     * @return 图片序列
     */
    public List<Image> getImageList(String name) {
        String frameInfo = getString(name, null);
        if (frameInfo == null)
            return new ArrayList<>();

        int delimiter = frameInfo.lastIndexOf("/");
        if (delimiter <= 0) {
            ZXLogger.warning("Invalid image list: " + frameInfo + "  --  " + this);
            return new ArrayList<>();
        }

        String path = frameInfo.substring(0, delimiter);
        String[] indexes = frameInfo.substring(delimiter + 1).split("-");
        int from = Integer.parseInt(indexes[0]);
        int to = Integer.parseInt(indexes[1]);
        List<Image> images = new ArrayList<>(to - from + 1);
        for (int i = from; i < to + 1; i++) {
            images.add(getImageFromPath(path + "-" + i + ".png"));
        }
        return images;
    }

    /**
     * 获取表达式向量
     *
     * @param name 属性名
     * @return 表达式向量
     */
    public ExpressionVector getExpressionVector(String name) {
        return new ExpressionVector(expressionCalculator, getString(name, "0,0"), index);
    }

    /**
     * 获取原件名
     *
     * @return 原件名 如'_sprite'
     */
    public String getName() {
        return name;
    }

    /**
     * 获取原件全名
     *
     * @return 原件名 如'_sprite-1'
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 获取原件索引
     *
     * @return 索引 如'_sprite-1' 索引为 0
     */
    public int getIndex() {
        return index;
    }
}
