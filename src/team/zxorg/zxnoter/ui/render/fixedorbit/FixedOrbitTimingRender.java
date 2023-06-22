package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.timing.Timing;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitObjectKey;

public class FixedOrbitTimingRender extends FixedOrbitRender {
    public FixedOrbitTimingRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme) {
        super(renderInfo, renderZXMap, canvas, theme);
    }


    @Override
    protected void renderHandle() {

        //绘制Timing点
        for (int i = 0; i < zxMap.timingPoints.size(); i++) {
            Timing timing = zxMap.timingPoints.get(i);
            if (timing.isNewBaseBpm) {//基准BPM
                loadImage(getImage(FixedOrbitObjectKey.TIMING_BASE));
            } else {
                loadImage(getImage(FixedOrbitObjectKey.TIMING));
            }

            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(timing.timestamp));
            drawImage();

        }
    }


}
