package team.zxorg.skin;

public enum ResolutionInfo {
    IPAD(1.333333333333333, "ipad mini", DeviceType.IOS),
    IPAD2(1.333984375, "ipad 12.9英寸", DeviceType.IOS),
    IPAD3(1.431654676258993, "ipad 11英寸", DeviceType.IOS),
    IPAD4(1.6, "ipad mini6", DeviceType.IOS),
    PHONE(1.706666666666667, "手机", DeviceType.ANDROID),
    PC(1.777777777777778, "电脑", DeviceType.WINDOWS);
    final double aspectRatio;
    final String name;
    final DeviceType deviceType;

    ResolutionInfo(double aspectRatio, String name, DeviceType deviceType) {
        this.aspectRatio = aspectRatio;
        this.name = name;
        this.deviceType = deviceType;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public String getName() {
        return name;
    }

    public DeviceType getDevice() {
        return deviceType;
    }

    @Override
    public String toString() {
        return  name + " " + String.format("%.4f", aspectRatio);
    }
}
