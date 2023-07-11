package team.zxorg.zxnoter.resource.type;

import team.zxorg.zxnoter.resource.ResourcePack;
import team.zxorg.zxnoter.resource.pack.*;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public enum ResourceType {
    color(ColorResourcePack.class),
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
            throw new RuntimeException(e);
        }
    }

    public final Class resourceClass;
}
