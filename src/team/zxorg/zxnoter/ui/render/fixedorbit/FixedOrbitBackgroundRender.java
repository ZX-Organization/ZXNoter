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


        image = getImage(FixedOrbitObjectKey.JUDGED_LINE);
        graphics.drawImage(image, 0, canvasHeight * getRenderInfo().judgedLinePosition - image.getHeight() / 2, canvasWidth, image.getHeight());

    }
}
