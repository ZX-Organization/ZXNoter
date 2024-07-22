package team.zxorg.mapedit.render;

import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import team.zxorg.fxcl.resource.ImageManager;

public enum RenderTexture {
    NOTE_END("end1"),
    NOTE_LEFT("left"),
    NOTE_LONG("long"),
    NOTE_NODE("node"),
    NOTE_NOTE("note"),
    NOTE_right("right"),
    NOTE_slide("slide"),



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
