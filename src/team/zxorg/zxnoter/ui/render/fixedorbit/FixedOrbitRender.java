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



    /**
     * 获取时间到y位置
     *
     * @param time
     * @return
     */
    public double getTimeToPosition(long time) {
        //判定线时间偏移
        long judgedLineTimeOffset = getJudgedLineTimeOffset();
        return (renderInfo.timelinePosition - time + judgedLineTimeOffset) * renderInfo.timelineZoom;
    }

    /**
     * 获取判断线时间偏移位置
     * @return 偏移时间
     */

    public long getJudgedLineTimeOffset() {
        return (long) (canvasHeight * getRenderInfo().judgedLinePosition / renderInfo.timelineZoom);
    }

    /**
     * 获取y位置到时间
     * @param y
     * @return
     */
    public long getPositionToTime(double y) {
        long topTime = (long) (getRenderInfo().timelinePosition - (canvasHeight - canvasHeight * getRenderInfo().judgedLinePosition) / renderInfo.timelineZoom);
        return (long) (topTime + (canvasHeight - y) / renderInfo.timelineZoom);
    }

}
