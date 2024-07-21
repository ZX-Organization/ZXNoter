package team.zxorg.zxnoter.api.extension;

@FunctionalInterface
public interface IExtensionInitializer {
    void onInitialize(IExtension extension);
}
