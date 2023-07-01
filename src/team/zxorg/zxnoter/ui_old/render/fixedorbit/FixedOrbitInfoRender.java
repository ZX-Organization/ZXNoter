package team.zxorg.zxnoter.ui_old.render.fixedorbit;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.timing.Timing;
import team.zxorg.zxnoter.ui_old.TimeUtils;
import team.zxorg.zxnoter.ui_old.render.Render;
import team.zxorg.zxnoter.ui_old.render.basis.RenderPoint;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitObjectKey;

import java.util.ArrayList;

public class FixedOrbitInfoRender extends FixedOrbitRender {
    public FixedOrbitInfoRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme, ArrayList<RenderBeat> renderBeats, ZXMap zxMap) {
        super(renderInfo, renderZXMap, canvas, theme);
        this.renderBeats = renderBeats;
        this.zxMap = zxMap;
    }

    ZXMap zxMap;

    ArrayList<RenderBeat> renderBeats;
    public boolean timeIsFormat = true;
    public RenderPoint point = new RenderPoint();

    public Timing selectTiming;

    @Override
    protected void renderHandle() {
        selectTiming = null;

        //绘制Timing点
        for (int i = 0; i < zxMap.timingPoints.size(); i++) {
            Timing timing = zxMap.timingPoints.get(i);
            if (timing.isNewBaseBpm) {//基准BPM
                loadImage(getImage(FixedOrbitObjectKey.TIMING_BASE));

                graphics.setFill(Color.WHEAT);
                graphics.setFont(new Font(16));
                graphics.setTextAlign(TextAlignment.LEFT);
                graphics.setTextBaseline(VPos.CENTER);
                graphics.fillText("bpm\n" + (int) (timing.absBpm * 100) / 100f, 0, getInfo().getTimeToPosition(timing.timestamp));

            } else {

                graphics.setFill(Color.WHEAT);
                graphics.setFont(new Font(16));
                graphics.setTextAlign(TextAlignment.RIGHT);
                graphics.setTextBaseline(VPos.CENTER);

                graphics.fillText((int) (timing.bpmSpeed / timing.absBpm * 100) / 100f + "×", getInfo().canvasWidth.getValue() / 2, getInfo().getTimeToPosition(timing.timestamp));
                loadImage(getImage(FixedOrbitObjectKey.TIMING));
            }
            renderRectangle.setX(HPos.RIGHT, getInfo().canvasWidth.getValue());
            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(timing.timestamp));
            if (renderRectangle.containPoint(point)) {
                selectTiming = timing;
                if (timing.isNewBaseBpm)
                    renderImage = getImage(FixedOrbitObjectKey.TIMING_DELETE);
                else
                    renderImage = getImage(FixedOrbitObjectKey.TIMING_BASE_DELETE);
            }

            drawImage();
        }


        for (RenderBeat renderBeat : renderBeats) {
            double beatCycleTime = 60000. / (renderBeat.timing.absBpm);

            for (int i = 1; i < renderBeat.measure; i++) {//分拍线
                /*loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE));
                renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
                renderRectangle.setY(VPos.CENTER,);
                drawImage();*/

             /*   long time = (long) (renderBeat.time + (beatCycleTime / renderBeat.measure) * i);
                graphics.setFill(Color.WHEAT);
                graphics.setFont(new Font(16));
                graphics.setTextAlign(TextAlignment.RIGHT);
                graphics.setTextBaseline(VPos.CENTER);
                graphics.fillText((timeIsFormat ? TimeUtils.formatTime(time) : time) + "", getInfo().canvasWidth.getValue() - 14, getInfo().getTimeToPosition(time));
*/
            }

            //绘制拍线
           /* loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE));
            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(renderBeat.time));
            drawImage();*/


/*            graphics.setFill(Color.WHEAT);
            graphics.setFont(new Font(16));
            graphics.setTextAlign(TextAlignment.RIGHT);
            graphics.setTextBaseline(VPos.CENTER);
            graphics.fillText((timeIsFormat ? TimeUtils.formatTime(renderBeat.time) : renderBeat.time) + "",
                    getInfo().canvasWidth.getValue() - 14,
                    getInfo().getTimeToPosition(renderBeat.time));*/


            graphics.setFill(Color.WHEAT);
            graphics.setFont(new Font(16));
            graphics.setTextAlign(TextAlignment.LEFT);
            graphics.setTextBaseline(VPos.CENTER);
            graphics.fillText("1/" + renderBeat.measure, 4, getInfo().getTimeToPosition(renderBeat.time + beatCycleTime / 2));


            // + " " + renderBeat.measure
        }
    }
}
