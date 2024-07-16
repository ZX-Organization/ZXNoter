package team.zxorg.zxnoter.util;

/**
 * 动态控制台输出
 */
public class DynamicConsoleOutput {
    private final StringBuilder sb = new StringBuilder();


    /**
     * 获取StringBuilder
     *
     * @return StringBuilder
     */
    public StringBuilder getStringBuilder() {
        return sb;
    }

    /**
     * 增加字符串
     *
     * @param v 字符串
     */
    public void appendString(String v) {
        sb.append(v);
    }

    /**
     * 增加字符串
     *
     * @param v 字符串
     */
    public void appendString(Object v) {
        sb.append(v);
    }

    /**
     * 增加格式化字符串
     *
     * @param format 格式化字符串
     * @param args   参数
     */
    public void appendFormat(String format, Object... args) {
        sb.append(String.format(format, args));
    }

    /**
     * 删除最后一个字符
     */
    public void deleteLastChar() {
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    /**
     * 删除指定位置的字符
     *
     * @param index 字符位置
     */
    public void deleteCharAt(int index) {
        if (index >= 0 && index < sb.length()) {
            sb.deleteCharAt(index);
        }
    }

    /**
     * 清除缓存内容
     */
    public void clear() {
        sb.setLength(0);
    }

    /**
     * 更新控制台输出
     */
    public void update() {
        int backspaces = sb.length() + 1;
        System.out.print("\u0008".repeat(backspaces) + sb);
        clear();
    }

    /**
     * 设置控制台输出颜色
     *
     * @param ansiCode ANSI 颜色码
     */
    public void setColor(ANSICode ansiCode) {
        sb.insert(0, ansiCode.toString());
    }

    /**
     * 重置控制台颜色
     */
    public void resetColor() {
        sb.append(ANSICode.RESET);
    }
}
