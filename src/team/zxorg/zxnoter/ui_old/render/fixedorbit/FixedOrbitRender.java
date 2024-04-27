package team.zxorg.zxnoter.ui_old.render.fixedorbit;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.info.map.ZXMInfo;
import team.zxorg.zxnoter.resource_old.ZXResources;
import team.zxorg.zxnoter.ui_old.render.Render;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitNotesKey;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitObjectKey;

public abstract class FixedOrbitRender extends Render {
    protected String theme;

    public FixedOrbitRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme) {
        super(renderInfo, renderZXMap, canvas);
        this.theme = theme;
        getOrbits();
    }

    @Override
    public FixedOrbitRenderInfo getInfo() {
        return (FixedOrbitRenderInfo) renderInfo;
    }

    public Image getImage(String state, FixedOrbitNotesKey notesKey) {

        return ZXResources.getImage("img.note.theme.default." + state + "." + notesKey.name);
    }

    public Image getImage(FixedOrbitObjectKey objectKey) {
        return ZXResources.getImage("img.note.theme.default." + objectKey.name);
    }

    public int getOrbits() {
        getInfo().orbits.set(Integer.parseInt(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.KeyCount)));
        return getInfo().orbits.get();
    }



}
