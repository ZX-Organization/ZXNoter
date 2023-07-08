package team.zxorg.zxnoter.resource.type;

import team.zxorg.zxnoter.resource.ResourcePack;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public enum ResourceType {
    color(ColorResource.class),
    icon(IconResource.class),
    language(LanguageResource.class),
    layout(LayoutResource.class),
    sound(SoundResource.class),
    style(StyleResource.class),
    ;

    ResourceType(Class resourceClass) {
        this.resourceClass = resourceClass;
    }

    public Resource build(ResourcePack pack, Path jsonPath) {
        try {
            return (Resource) resourceClass.getDeclaredConstructor(ResourcePack.class, ResourceType.class, Path.class).newInstance(pack, this, jsonPath);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public final Class resourceClass;
}
