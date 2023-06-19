package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.timing.Timing;
import team.zxorg.zxnoter.ui.TimeUtils;
import team.zxorg.zxnoter.ui.render.basis.RenderRectangle;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitNotesKey;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitObjectKey;

public class FixedOrbitBackgroundRender extends FixedOrbitRender {

    //分拍数 Minutes


    public FixedOrbitBackgroundRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme) {
        super(renderInfo, renderZXMap, canvas, theme);
    }

    @Override
    protected void renderHandle() {
        Image image;


        loadRenderImage(getImage(FixedOrbitObjectKey.LOGO));
        renderRectangleScale((renderInfo.canvasWidth.get() * 0.8), Orientation.HORIZONTAL);
        renderRectangle.setRelativePosition(canvasRectangle, Pos.CENTER);
        drawImage();


        //绘制轨道
        loadRenderImage(getImage(FixedOrbitObjectKey.ORBIT_LINE));
        //计算尺寸
        renderRectangle.setHeight(canvasRectangle.getHeight());
        renderRectangleSetY(0);
        double orbitWidth = (renderInfo.canvasWidth.get() / orbits);
        for (int i = 1; i < orbits; i++) {
            //计算位置
            renderRectangleSetX(orbitWidth * i);
            //X偏移中间
            renderRectangleOffsetPositionByHalf(Pos.CENTER_LEFT);
            drawImage();
        }




        //绘制Timing点

        for (int i = 0; i < zxMap.timingPoints.size(); i++) {
            Timing timing = zxMap.timingPoints.get(i);
            if (timing.isNewBaseBpm) {//基准BPM
                loadRenderImage(getImage(FixedOrbitObjectKey.TIMING_BASE));
            } else {
                loadRenderImage(getImage(FixedOrbitObjectKey.TIMING));
            }

            renderRectangle.setY(getRenderInfo().getTimeToPosition(timing.timestamp));
            renderRectangle.setX(0);
            renderRectangle.offsetPositionByHalf(Pos.TOP_CENTER);
            drawImage();

        }


        //绘制判定线
        image = getImage(FixedOrbitObjectKey.JUDGED_LINE);
        graphics.drawImage(image, 0, renderInfo.canvasHeight.get() * getRenderInfo().judgedLinePositionPercentage.doubleValue() - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

        //绘制底部线
        image = getImage(FixedOrbitObjectKey.BOTTOM_LINE);
        graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(0) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

        //绘制头部线
        image = getImage(FixedOrbitObjectKey.TOP_LINE);
        graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(getLastTime() + getRenderInfo().judgedLinePositionTimeOffset.get()) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

    }



}
