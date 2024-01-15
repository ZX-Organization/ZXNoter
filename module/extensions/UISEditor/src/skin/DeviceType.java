package skin;

public enum DeviceType {
    MAC("Mac"), WINDOWS("Win"), IOS("平板"), ANDROID("手机");
    final String name;

    DeviceType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
