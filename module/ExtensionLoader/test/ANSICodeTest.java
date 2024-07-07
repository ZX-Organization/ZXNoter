import team.zxorg.extensionloader.util.ANSICode;

public class ANSICodeTest {
    public static void main(String[] args) {

        // 测试文本颜色
        System.out.println(ANSICode.RED + "这是红色文本" + ANSICode.RESET);
        System.out.println(ANSICode.GREEN + "这是绿色文本" + ANSICode.RESET);
        System.out.println(ANSICode.BLUE + "这是蓝色文本" + ANSICode.RESET);

        // 测试背景颜色
        System.out.println(ANSICode.BG_YELLOW + "这是黄色背景" + ANSICode.RESET);
        System.out.println(ANSICode.BG_CYAN + "这是青色背景" + ANSICode.RESET);

        // 测试文本样式
        System.out.println(ANSICode.BOLD + "这是粗体文本" + ANSICode.RESET);
        System.out.println(ANSICode.UNDERLINE + "这是下划线文本" + ANSICode.RESET);
        System.out.println(ANSICode.ITALIC + "这是斜体文本" + ANSICode.RESET);

        // 测试组合样式
        System.out.println(ANSICode.combine(ANSICode.RED, ANSICode.BG_WHITE, ANSICode.BOLD)
                + "这是红色文本,白色背景,粗体" + ANSICode.RESET);

        // 测试RGB颜色
        System.out.println(ANSICode.foregroundColor(255, 128, 0) + "这是自定义橙色文本" + ANSICode.RESET);
        System.out.println(ANSICode.backgroundColor(128, 0, 128) + "这是自定义紫色背景" + ANSICode.RESET);

        // 测试应用样式方法
        System.out.println(ANSICode.applyStyle("这是应用样式的文本", ANSICode.BLUE, ANSICode.BG_BRIGHT_WHITE, ANSICode.UNDERLINE));

        // 测试重置特定样式
        System.out.println(ANSICode.resetStyle(ANSICode.RED + "这是红色文本,但会被重置"));
    }

}
