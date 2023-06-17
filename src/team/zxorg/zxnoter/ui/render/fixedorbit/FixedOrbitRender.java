package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
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
        return Integer.parseInt(zxMap.unLocalizedMapInfo.getInfo("KeyCount"));
    }

    /**
     * 获取最后的键位置
     *
     * @return 时间戳
     */
    public long getLastTime() {
        BaseNote lastNote = zxMap.notes.get(zxMap.notes.size() - 1);
        long time;
        if (lastNote instanceof ComplexNote complexNote) {
            time = complexNote.notes.get(complexNote.notes.size() - 1).timeStamp;
        } else if (lastNote instanceof LongNote longNote) {
            time = longNote.timeStamp + longNote.sustainedTime;
        } else {
            time = zxMap.notes.get(zxMap.notes.size() - 1).timeStamp;
        }
        getRenderInfo().noteLastTime.set(time);
        return time;
    }


}
