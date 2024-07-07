package team.zxorg.zxnoter_old.ui.render.fixedorbit;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter_old.map.ZXMap;
import team.zxorg.zxnoter_old.ui.render.fixedorbit.key.FixedOrbitObjectKey;

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




    }


}
