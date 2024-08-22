import team.zxorg.zxnoter.core.Version;

public class VersionTest {
    public static void main(String[] args) {
        // 测试构造函数和 toString 方法
        testConstruction();

        // 测试版本比较
        testVersionComparison();

        // 测试 isSupported 方法
        testIsSupported();

        // 测试 resolve 方法
        testResolve();

        // 测试特殊情况
        testEdgeCases();
    }

    private static void testConstruction() {
        System.out.println("测试构造函数和 toString 方法:");
        Version v1 = new Version("1.2.3-beta");
        System.out.println("Version 1: " + v1);

        Version v2 = new Version(">= 2.0.0");
        System.out.println("Version 2: " + v2);

        Version v3 = new Version(3, 1, 4, Version.ReleaseStatus.RC);
        System.out.println("Version 3: " + v3);

        System.out.println();
    }

    private static void testVersionComparison() {
        System.out.println("测试版本比较:");
        Version v1 = new Version("1.2.3");
        Version v2 = new Version("1.2.4");
        Version v3 = new Version("1.2.3-beta");

        System.out.println("1.2.3 比 1.2.4 小: " + (v1.compareTo(v2) < 0));
        System.out.println("1.2.3 等于 1.2.3: " + (v1.compareTo(new Version("1.2.3")) == 0));
        System.out.println("1.2.3 比 1.2.3-beta 大: " + (v1.compareTo(v3) > 0));

        System.out.println();
    }

    private static void testIsSupported() {
        System.out.println("测试 isSupported 方法:");
        Version requirement = new Version(">= 2.0.0");
        Version v1 = new Version("1.9.9");
        Version v2 = new Version("2.0.0");
        Version v3 = new Version("2.1.0");

        System.out.println("1.9.9 满足 >= 2.0.0: " + requirement.isSupported(v1));
        System.out.println("2.0.0 满足 >= 2.0.0: " + requirement.isSupported(v2));
        System.out.println("2.1.0 满足 >= 2.0.0: " + requirement.isSupported(v3));

        System.out.println();
    }

    private static void testResolve() {
        System.out.println("测试 resolve 方法:");
        int versionCode = 1002003;  // 表示 1.2.3
        Version resolved = Version.resolve(versionCode);
        System.out.println("Resolved version from code " + versionCode + ": " + resolved);

        System.out.println();
    }

    private static void testEdgeCases() {
        System.out.println("测试特殊情况:");
        try {
            new Version("invalid");
        } catch (IllegalArgumentException e) {
            System.out.println("成功捕获无效版本字符串: " + e.getMessage());
        }

        try {
            new Version("1.2.3.4");
        } catch (IllegalArgumentException e) {
            System.out.println("成功捕获过多版本号部分: " + e.getMessage());
        }

        Version v1 = new Version("1.0.0");
        Version v2 = new Version("1.0.0-alpha");
        System.out.println("1.0.0 比 1.0.0-alpha 大: " + (v1.compareTo(v2) > 0));

        System.out.println();
    }
}