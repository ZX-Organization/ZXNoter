package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.timing.Timing;
import team.zxorg.zxnoter.ui.TimeUtils;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitObjectKey;

public class FixedOrbitBeatLineRender extends FixedOrbitRender {

    public int beats = 12;

    public FixedOrbitBeatLineRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme) {
        super(renderInfo, renderZXMap, canvas, theme);
    }

    @Override
    protected void renderHandle() {
//绘制拍线和分拍线
        double beatCycleTime;
        long beatTime;


        for (long time = getRenderInfo().getPositionToTime(canvas.getHeight()); time < getRenderInfo().getPositionToTime(0); time++) {
            Timing timing = findAfterTiming(time);
            beatCycleTime = 60000. / (timing.absBpm);

            if (time < 0)
                continue;


            //拥有基准
            beatTime = time;

            //绘制分拍
            if ((beatTime - timing.timestamp) % (beatCycleTime / beats) < 1) {
                loadRenderImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE));
                renderRectangle.setWidth(canvasRectangle.getWidth());
                renderRectangle.setX(0);
                renderRectangle.setY(getRenderInfo().getTimeToPosition(beatTime));
                renderRectangleOffsetPositionByHalf(Pos.TOP_CENTER);
                drawImage();
                graphics.setFill(Color.WHEAT);
                graphics.fillText(TimeUtils.formatTime(time), 0, getRenderInfo().getTimeToPosition(time));
            }

            //绘制拍
            if ((beatTime - timing.timestamp) % beatCycleTime < 1) {
                loadRenderImage(getImage(FixedOrbitObjectKey.BEAT_LINE));
                renderRectangle.setWidth(canvasRectangle.getWidth());
                renderRectangle.setX(0);
                renderRectangle.setY(getRenderInfo().getTimeToPosition(beatTime));
                renderRectangleOffsetPositionByHalf(Pos.TOP_CENTER);
                drawImage();
                graphics.setFill(Color.WHEAT);
                graphics.fillText(TimeUtils.formatTime(time), 0, getRenderInfo().getTimeToPosition(time));
            }

        }
    }
}
