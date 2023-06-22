package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitObjectKey;

public class FixedOrbitBackgroundRender extends FixedOrbitRender {

    //分拍数 Minutes


    public FixedOrbitBackgroundRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme) {
        super(renderInfo, renderZXMap, canvas, theme);
    }

    @Override
    protected void renderHandle() {
        Image image;


        loadImage(getImage(FixedOrbitObjectKey.LOGO));
        renderRectangle.scale(Pos.CENTER, (renderInfo.canvasWidth.get() * 0.8), Orientation.HORIZONTAL);
        renderRectangle.setXY(Pos.CENTER, canvasRectangle.getCenterX(), canvasRectangle.getCenterY());
        drawImage();


        //绘制轨道
        loadImage(getImage(FixedOrbitObjectKey.ORBIT_LINE));
        //计算尺寸
        renderRectangle.setHeight(VPos.TOP, canvasRectangle.getHeight());
        for (int i = 1; i < getInfo().orbits.get(); i++) {
            //计算位置
            renderRectangle.setX(HPos.CENTER, getInfo().orbitWidth.get() * i);
            drawImage();
        }


        //绘制判定线
        image = getImage(FixedOrbitObjectKey.JUDGED_LINE);
        graphics.drawImage(image, 0, renderInfo.canvasHeight.get() * getInfo().judgedLinePositionPercentage.doubleValue() - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

        //绘制底部线
        image = getImage(FixedOrbitObjectKey.BOTTOM_LINE);
        graphics.drawImage(image, 0, getInfo().getTimeToPosition(0) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

        //绘制头部线
        image = getImage(FixedOrbitObjectKey.TOP_LINE);
        graphics.drawImage(image, 0, getInfo().getTimeToPosition(getLastTime() + getInfo().judgedLinePositionTimeOffset.get()) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

    }


}
