package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.note.timing.Timing;
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
        getRenderInfo().orbits.set(Integer.parseInt(zxMap.unLocalizedMapInfo.getInfo("KeyCount")));
        return getRenderInfo().orbits.get();
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


    /**
     * 寻找之后的Timing
     *
     * @return
     */
    public Timing findAfterTiming(long time) {
        //找到上一个基准timing
        Timing baseTiming = new Timing(0, 1, false, 0);
        Timing timing = new Timing(0, 1, false, 0);
        for (int i = 0; i < zxMap.timingPoints.size(); i++) {
            if (timing.isNewBaseBpm)
                baseTiming = timing;
            if (zxMap.timingPoints.get(i).timestamp > time) {
                return baseTiming;
            }
            timing = zxMap.timingPoints.get(i);

        }
        return timing;
    }


}
