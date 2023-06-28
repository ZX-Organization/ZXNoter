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
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitObjectKey;

import java.util.ArrayList;

public class FixedOrbitInfoRender extends FixedOrbitRender {
    public FixedOrbitInfoRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme, ArrayList<RenderBeat> renderBeats) {
        super(renderInfo, renderZXMap, canvas, theme);
        this.renderBeats = renderBeats;
    }

    ArrayList<RenderBeat> renderBeats;
    public boolean timeIsFormat = true;

    @Override
    protected void renderHandle() {
        for (RenderBeat renderBeat : renderBeats) {
            double beatCycleTime = 60000. / (renderBeat.timing.absBpm);

            for (int i = 1; i < renderBeat.measure; i++) {//分拍线
                /*loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE));
                renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
                renderRectangle.setY(VPos.CENTER,);
                drawImage();*/

                long time = (long) (renderBeat.time + (beatCycleTime / renderBeat.measure) * i);
                graphics.setFill(Color.WHEAT);
                graphics.setFont(new Font(16));
                graphics.setTextAlign(TextAlignment.RIGHT);
                graphics.setTextBaseline(VPos.CENTER);
                graphics.fillText((timeIsFormat ? TimeUtils.formatTime(time) : time) + "", getInfo().canvasWidth.getValue() - 4, getInfo().getTimeToPosition(time));

            }

            //绘制拍线
           /* loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE));
            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(renderBeat.time));
            drawImage();*/
            //绘制Timing点
            for (int i = 0; i < zxMap.timingPoints.size(); i++) {
                Timing timing = zxMap.timingPoints.get(i);
                if (timing.isNewBaseBpm) {//基准BPM
                    loadImage(getImage(FixedOrbitObjectKey.TIMING_BASE));
                } else {
                    loadImage(getImage(FixedOrbitObjectKey.TIMING));
                }
                renderRectangle.setX(HPos.RIGHT, getInfo().canvasWidth.getValue());
                renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(timing.timestamp));
                drawImage();

            }


            graphics.setFill(Color.WHEAT);
            graphics.setFont(new Font(16));
            graphics.setTextAlign(TextAlignment.RIGHT);
            graphics.setTextBaseline(VPos.CENTER);
            graphics.fillText((timeIsFormat ? TimeUtils.formatTime(renderBeat.time) : renderBeat.time) + "", getInfo().canvasWidth.getValue() - 14, getInfo().getTimeToPosition(renderBeat.time));


            graphics.setFill(Color.WHEAT);
            graphics.setFont(new Font(16));
            graphics.setTextAlign(TextAlignment.LEFT);
            graphics.setTextBaseline(VPos.CENTER);
            graphics.fillText("1/" + renderBeat.measure, 4, getInfo().getTimeToPosition(renderBeat.time + beatCycleTime / 2));


            // + " " + renderBeat.measure
        }
    }
}
