package team.zxorg.extensionloader.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VersionChecker {
    private final List<Version> versions = new ArrayList<>();

    @Override
    public String toString() {
        return versions.stream()
                .map(Version::toString)
                .collect(Collectors.joining(", "));
    }

    public VersionChecker(String versionRange) {
        String[] rangeParts = versionRange.split(",");
        for (String rangePart : rangeParts) {
            Version version = new Version(rangePart.trim());
            versions.add(version);
        }
    }

    public boolean isSupported(Version version) {
        return versions.stream().anyMatch(v -> v.isSupported(version));
    }
}
