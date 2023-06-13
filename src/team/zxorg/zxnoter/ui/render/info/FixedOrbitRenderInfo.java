package team.zxorg.zxnoter.ui.render.info;

import javafx.scene.image.Image;
import team.zxorg.zxnoter.resource.ZXResources;

public class FixedOrbitRenderInfo extends RenderInfo {
    public Image judgedLineImage = ZXResources.getImage("img.graphics.note.judgedline");
    public Image leftImage = ZXResources.getImage("img.graphics.note.left");
    public Image longImage = ZXResources.getImage("img.graphics.note.long");
    public Image nodeImage = ZXResources.getImage("img.graphics.note.node");
    public Image noteImage = ZXResources.getImage("img.graphics.note.note");
    public Image rightImage = ZXResources.getImage("img.graphics.note.right");
    public Image slideImage = ZXResources.getImage("img.graphics.note.slide");

    /**
     * 轨道数
     */
    public int orbits = 0;

    /**
     * 判定线偏移
     */
    public long judgedLine = 10;
}
