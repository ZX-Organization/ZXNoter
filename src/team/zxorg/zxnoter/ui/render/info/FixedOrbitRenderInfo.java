package team.zxorg.zxnoter.ui.render.info;

import javafx.scene.image.Image;
import team.zxorg.zxnoter.resource.ZXResources;

public class FixedOrbitRenderInfo extends RenderInfo {
    public Image judgedLineImage;
    public Image leftImage;
    public Image longImage;
    public Image nodeImage;
    public Image endImage;
    public Image noteImage;
    public Image rightImage;
    public Image slideImage;

    public FixedOrbitRenderInfo(String key) {
        if (key == null)
            key = "default";
        judgedLineImage = ZXResources.getImage("img.note.theme." + key + ".judgedline");
        leftImage = ZXResources.getImage("img.note.theme." + key + ".left");
        longImage = ZXResources.getImage("img.note.theme." + key + ".long");
        nodeImage = ZXResources.getImage("img.note.theme." + key + ".node");
        endImage = ZXResources.getImage("img.note.theme." + key + ".end");
        noteImage = ZXResources.getImage("img.note.theme." + key + ".note");
        rightImage = ZXResources.getImage("img.note.theme." + key + ".right");
        slideImage = ZXResources.getImage("img.note.theme." + key + ".slide");
    }

    /**
     * 轨道数
     */
    public int orbits = 0;

    /**
     * 判定线偏移百分比
     */
    public float judgedLinePosition = 0.95f;
}
