package team.zxorg.zxnoter.ui_old.render.fixedorbit;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.map.mapInfo.ZXMInfo;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui_old.render.Render;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitNotesKey;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitObjectKey;

public abstract class FixedOrbitRender extends Render {
    String theme;//主题

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

        return ZXResources.getImage("img.note.theme." + theme + "." + state + "." + notesKey.name);
    }

    public Image getImage(FixedOrbitObjectKey objectKey) {
        return ZXResources.getImage("img.note.theme." + theme + "." + objectKey.name);
    }

    public int getOrbits() {
        getInfo().orbits.set(Integer.parseInt(zxMap.unLocalizedMapInfo.getInfo(ZXMInfo.KeyCount)));
        return getInfo().orbits.get();
    }



}
