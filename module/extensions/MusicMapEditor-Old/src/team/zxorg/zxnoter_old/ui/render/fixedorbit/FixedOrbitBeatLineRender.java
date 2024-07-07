package team.zxorg.zxnoter_old.ui.render.fixedorbit;

import javafx.beans.property.LongProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter_old.map.ZXMap;
import team.zxorg.zxnoter_old.ui.render.fixedorbit.key.FixedOrbitObjectKey;

import java.util.ArrayList;

public class FixedOrbitBeatLineRender extends FixedOrbitRender {

    final Image SUB_BEAT_LINE_R;
    final Image SUB_BEAT_LINE_B;
    final Image SUB_BEAT_LINE_P;
    final Image SUB_BEAT_LINE_Y;
    final Image SUB_BEAT_LINE;
    final Image BEAT_LINE5;
    final Image BOTTOM_LINE;
    final Image TOP_LINE;
    final Image JUDGED_LINE;

    public boolean showSubBeats = false;
    ArrayList<RenderBeat> renderBeats;
    LongProperty mapTimeLength;//谱面时长

    public FixedOrbitBeatLineRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme, ArrayList<RenderBeat> renderBeats, LongProperty mapTimeLength) {
        super(renderInfo, renderZXMap, canvas, theme);
        this.renderBeats = renderBeats;
        this.mapTimeLength = mapTimeLength;

        SUB_BEAT_LINE_R = getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_R);
        SUB_BEAT_LINE_B = getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_B);
        SUB_BEAT_LINE_P = getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_P);
        SUB_BEAT_LINE_Y = getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_Y);
        SUB_BEAT_LINE = getImage(FixedOrbitObjectKey.SUB_BEAT_LINE);
        BEAT_LINE5 = getImage(FixedOrbitObjectKey.BEAT_LINE5);
        BOTTOM_LINE = getImage(FixedOrbitObjectKey.BOTTOM_LINE);
        TOP_LINE = getImage(FixedOrbitObjectKey.TOP_LINE);
        JUDGED_LINE = getImage(FixedOrbitObjectKey.JUDGED_LINE);
    }

    @Override
    protected void renderHandle() {
        //绘制拍线和分拍线

        for (RenderBeat renderBeat : renderBeats) {
            double beatCycleTime = 60000. / (renderBeat.timing.absBpm);

            if (showSubBeats)
                for (int i = 1; i < renderBeat.measure; i++) {//分拍线
                    //loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE));
                    switch (renderBeat.measure) {
                        case 2 -> {
                            loadImage(SUB_BEAT_LINE_R);
                        }
                        case 4 -> {
                            if (i == 2) {
                                loadImage(SUB_BEAT_LINE_R);
                            } else {
                                loadImage(SUB_BEAT_LINE_B);
                            }
                        }
                        case 6 -> {
                            if (i == 3) {
                                loadImage(SUB_BEAT_LINE_R);
                            } else {
                                loadImage(SUB_BEAT_LINE_P);
                            }
                        }
                        case 8 -> {
                            // 黄蓝黄红黄蓝黄
                            if ((i - 1) % 2 == 0) {
                                loadImage(SUB_BEAT_LINE_Y);
                            } else {
                                if (i == 4) {
                                    loadImage(SUB_BEAT_LINE_R);
                                } else {
                                    loadImage(SUB_BEAT_LINE_B);
                                }
                            }


                        }
                        default -> {
                            loadImage(SUB_BEAT_LINE);
                        }
                    }
                    renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
                    renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(renderBeat.time + (beatCycleTime / renderBeat.measure) * i));
                    drawImage();
                }

            //绘制拍线
            //loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE));
            /*if (a % 3 == 0)
                loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE1));
            if (a % 3 == 1)
                loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE2));
            if (a % 3 == 2)
                loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE3));*/

            if ("default".equals(theme))
                loadImage(BEAT_LINE5);
            else if ("preview".equals(theme))
                loadImage(SUB_BEAT_LINE);

            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(renderBeat.time));
            drawImage();


            //绘制底部线
            loadImage(BOTTOM_LINE);
            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(0));
            //image = getImage(FixedOrbitObjectKey.BOTTOM_LINE);
            //graphics.drawImage(image, 0, getInfo().getTimeToPosition(0) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
            drawImage();

            //绘制头部线
            loadImage(TOP_LINE);
            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, getInfo().getTimeToPosition(mapTimeLength.get() + getInfo().judgedLinePositionTimeOffset.get()));
            //image = getImage(FixedOrbitObjectKey.TOP_LINE);
            //graphics.drawImage(image, 0, getInfo().getTimeToPosition(mapTimeLength.get() + getInfo().judgedLinePositionTimeOffset.get()) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
            drawImage();


            //绘制判定线
            loadImage(JUDGED_LINE);
            renderRectangle.setWidth(HPos.LEFT, canvasRectangle.getWidth());
            renderRectangle.setY(VPos.CENTER, renderInfo.canvasHeight.get() * getInfo().judgedLinePositionPercentage.doubleValue());
            //graphics.drawImage(image, 0, renderInfo.canvasHeight.get() * getInfo().judgedLinePositionPercentage.doubleValue() - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
            drawImage();
        }
    }


}
