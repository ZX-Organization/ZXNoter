package team.zxorg.zxnoter.ui_old.render.fixedorbit;

import javafx.beans.property.LongProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import team.zxorg.zxnoter.map.ZXMap;
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

        for (RenderBeat renderBeat : renderBeats) {
            double beatCycleTime = 60000. / (renderBeat.timing.absBpm);

            if (showSubBeats)
                for (int i = 1; i < renderBeat.measure; i++) {//分拍线
                    //loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE));
                    switch (renderBeat.measure) {
                        case 2 -> {
                            loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_R));
                        }
                        case 4 -> {
                            if (i == 2) {
                                loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_R));
                            } else {
                                loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_B));
                            }
                        }
                        case 6 -> {
                            if (i == 3) {
                                loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_R));
                            } else {
                                loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_P));
                            }
                        }
                        case 8 -> {
                            // 黄蓝黄红黄蓝黄
                            if ((i - 1) % 2 == 0) {
                                loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_Y));
                            } else {
                                if (i == 4) {
                                    loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_R));
                                } else {
                                    loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE_B));
                                }
                            }


                        }
                        default -> {
                            loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE));
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
                loadImage(getImage(FixedOrbitObjectKey.BEAT_LINE5));
           else if ("preview".equals(theme))
                loadImage(getImage(FixedOrbitObjectKey.SUB_BEAT_LINE));

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
