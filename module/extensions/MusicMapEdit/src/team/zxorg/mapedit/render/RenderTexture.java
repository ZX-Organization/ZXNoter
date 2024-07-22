package team.zxorg.mapedit.render;

import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import team.zxorg.fxcl.resource.ImageManager;

public enum RenderTexture {
    MAP_END("end"),
    MAP_LEFT("left"),
    MAP_LONG("long"),
    MAP_NODE("node"),
    MAP_NOTE("note"),
    MAP_right("right"),
    MAP_slide("slide"),



    LOGO("logo"),
    ;
    private static final String Key = "musicMapEditor";
    private final ObjectProperty<Image> texture;

    RenderTexture(String key) {
        this.texture = ImageManager.getImage(Key + "." + key);
    }

    public Image get() {
        return texture.get();
    }

}
