package team.zxorg.zxnoter.ui_old.render.fixedorbit;

import javafx.beans.property.LongProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.ui_old.TimeUtils;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitObjectKey;

import java.util.ArrayList;

public class FixedOrbitBeatLineRender extends FixedOrbitRender {

    ArrayList<RenderBeat> renderBeats;
    LongProperty mapTimeLength;//谱面时长
    public boolean showSubBeats = false;

    public FixedOrbitBeatLineRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme, ArrayList<RenderBeat> renderBeats, LongProperty mapTimeLength) {
        super(renderInfo, renderZXMap, canvas, theme);
        this.renderBeats = renderBeats;
        this.mapTimeLength = mapTimeLength;

    }

    @Override
    protected void renderHandle() {
        //绘制拍线和分拍线

        int a = 0;

        for (RenderBeat renderBeat : renderBeats) {
            a++;
            double beatCycleTime = 60000. / (renderBeat.timing.absBpm);

            if (showSubBeats)
                for (int i = 0; i < renderBeat.measure; i++) {//分拍线
                    loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE));
                    renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
                    renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(renderBeat.time + (beatCycleTime / renderBeat.measure) * i));
                    drawImage();
                }

            //绘制拍线
            //loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE));
            if (a % 3 == 0)
                loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE1));
            if (a % 3 == 1)
                loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE2));
            if (a % 3 == 2)
                loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE3));
            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(renderBeat.time));
            drawImage();


            //绘制底部线
            loadImage(getImage(FixedOrbitObjectKey.BOTTOM_LINE));
            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(0));
            //image = getImage(FixedOrbitObjectKey.BOTTOM_LINE);
            //graphics.drawImage(image, 0, getInfo().getTimeToPosition(0) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
            drawImage();

            //绘制头部线
            loadImage(getImage(FixedOrbitObjectKey.TOP_LINE));
            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(mapTimeLength.get() + getInfo().judgedLinePositionTimeOffset.get()));
            //image = getImage(FixedOrbitObjectKey.TOP_LINE);
            //graphics.drawImage(image, 0, getInfo().getTimeToPosition(mapTimeLength.get() + getInfo().judgedLinePositionTimeOffset.get()) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
            drawImage();


            //绘制判定线
            loadImage(getImage(FixedOrbitObjectKey.JUDGED_LINE));
            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, renderInfo.canvasHeight.get() * getInfo().judgedLinePositionPercentage.doubleValue());
            //graphics.drawImage(image, 0, renderInfo.canvasHeight.get() * getInfo().judgedLinePositionPercentage.doubleValue() - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
            drawImage();
        }
    }


}
