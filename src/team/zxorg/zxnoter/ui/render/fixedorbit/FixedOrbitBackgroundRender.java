package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitNotesKey;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitObjectKey;

public class FixedOrbitBackgroundRender extends FixedOrbitRender {


    public FixedOrbitBackgroundRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme) {
        super(renderInfo, renderZXMap, canvas, theme);
    }

    @Override
    protected void renderHandle() {
        Image image;

        //绘制轨道
        image = getImage(FixedOrbitObjectKey.ORBIT);
        for (int i = 0; i < orbits; i++) {
            graphics.drawImage(image, i * (canvasWidth / orbits), 0, canvasWidth / orbits, canvasHeight);
        }

        //绘制判定线
        image = getImage(FixedOrbitObjectKey.JUDGED_LINE);
        graphics.drawImage(image, 0, canvasHeight * getRenderInfo().judgedLinePosition - image.getHeight() / 2, canvasWidth, image.getHeight());

        //绘制底部线
        image = getImage(FixedOrbitObjectKey.BOTTOM_LINE);

        graphics.drawImage(image, 0, getTimeToPosition(0) - image.getHeight() / 2, canvasWidth, image.getHeight());

    }
}
