package team.zxorg.zxnoter.resource;

import team.zxorg.zxnoter.ZXLogger;
import team.zxorg.zxnoter.resource.pack.*;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public enum ResourceType {
    theme(ThemeResourcePack.class),
    icon(IconResourcePack.class),
    language(LanguageResourcePack.class),
    layout(LayoutResourcePack.class),
    sound(SoundResourcePack.class),
    style(StyleResourcePack.class),
    ;

    ResourceType(Class resourceClass) {
        this.resourceClass = resourceClass;
    }

    public BaseResourcePack build(ResourcePack pack, Path jsonPath) {
        try {
            return (BaseResourcePack) resourceClass.getDeclaredConstructor(ResourcePack.class, ResourceType.class, Path.class).newInstance(pack, this, jsonPath);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            ZXLogger.warning("构建资源包时发生异常");
            throw new RuntimeException(e);
        }
    }

    public final Class resourceClass;
}