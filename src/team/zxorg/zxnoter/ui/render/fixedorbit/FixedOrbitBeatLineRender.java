package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.ui.TimeUtils;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitObjectKey;

import java.util.ArrayList;

public class FixedOrbitBeatLineRender extends FixedOrbitRender {

    ArrayList<RenderBeat> renderBeats;

    public FixedOrbitBeatLineRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme, ArrayList<RenderBeat> renderBeats) {
        super(renderInfo, renderZXMap, canvas, theme);
        this.renderBeats = renderBeats;
    }

    @Override
    protected void renderHandle() {
        //绘制拍线和分拍线

        for (RenderBeat renderBeat : renderBeats) {
            double beatCycleTime = 60000. / (renderBeat.timing.absBpm);
            for (int i = 0; i < renderBeat.measure; i++) {
                loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE));
                renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
                renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(renderBeat.time + (beatCycleTime / renderBeat.measure) * i));
                drawImage();
            }

            //绘制拍
            loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE));
            renderRectangle.setWidth(HPos.LEFT,canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER,getInfo().getTimeToPosition(renderBeat.time));
            drawImage();
            graphics.setFill(Color.WHEAT);
            graphics.fillText(TimeUtils.formatTime(renderBeat.time), 0, getInfo().getTimeToPosition(renderBeat.time));

        }
    }


}
