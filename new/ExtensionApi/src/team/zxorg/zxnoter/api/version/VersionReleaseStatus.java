package team.zxorg.zxnoter.api.version;

public enum VersionReleaseStatus {
    EMPTY(null),
    ALPHA("alpha"),
    BETA("beta"),
    RC("rc"),
    STABLE("stable");

    private final String sign;

    VersionReleaseStatus(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
