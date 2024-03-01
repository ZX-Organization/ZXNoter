package team.zxorg.extensionloader.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum PlatformType {
    MAC_ARM("mac", "aarch64"),
    MAC("mac"),
    LINUX_ARM("linux", "aarch64"),
    LINUX("linux"),
    WINDOWS("win"),
    ALL("");
    public static final PlatformType CURRENT_PLATFORM = detectCurrentPlatform();
    private final Set<String> keywords;

    public Set<String> getKeywords() {
        return keywords;
    }

    PlatformType(String... keywords) {
        this.keywords = new HashSet<>(List.of(keywords));
    }

    public boolean isMac() {
        return keywords.contains("mac");
    }

    public boolean isLinux() {
        return keywords.contains("linux");
    }

    public boolean isWindows() {
        return keywords.contains("win");
    }

    public boolean isArm() {
        return keywords.contains("aarch64");
    }

    public boolean isAll() {
        return this == ALL;
    }

    private static PlatformType detectCurrentPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        if (osName.contains("mac")) {
            return arch.contains("aarch64") ? MAC_ARM : MAC;
        } else if (osName.contains("linux")) {
            return arch.contains("aarch64") ? LINUX_ARM : LINUX;
        } else if (osName.contains("windows")) {
            return WINDOWS;
        } else {
            return ALL;
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
