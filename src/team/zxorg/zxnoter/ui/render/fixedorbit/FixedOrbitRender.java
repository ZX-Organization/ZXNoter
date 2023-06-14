package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.render.Render;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitNotesKey;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitObjectKey;

public abstract class FixedOrbitRender extends Render {
    String theme;//主题
    public int orbits;

    public FixedOrbitRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme) {
        super(renderInfo, renderZXMap, canvas);
        this.theme = theme;
        orbits = getOrbits();
    }

    @Override
    public FixedOrbitRenderInfo getRenderInfo() {
        return (FixedOrbitRenderInfo) renderInfo;
    }

    public Image getImage(String state, FixedOrbitNotesKey notesKey) {
        return ZXResources.getImage("img.note.theme." + theme + "." + state + "." + notesKey.name);
    }

    public Image getImage(FixedOrbitObjectKey objectKey) {
        return ZXResources.getImage("img.note.theme." + theme + "." + objectKey.name);
    }

    private int getOrbits() {
        return Integer.parseInt(renderZXMap.unLocalizedMapInfo.getInfo("KeyCount"));
    }

}
