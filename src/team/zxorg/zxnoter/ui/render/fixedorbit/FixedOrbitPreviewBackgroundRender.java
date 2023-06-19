package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.timing.Timing;
import team.zxorg.zxnoter.ui.render.fixedorbit.key.FixedOrbitObjectKey;

public class FixedOrbitPreviewBackgroundRender extends FixedOrbitRender {
    public FloatProperty mainJudgedLinePositionPercentage = new SimpleFloatProperty();

    public FixedOrbitPreviewBackgroundRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme) {
        super(renderInfo, renderZXMap, canvas, theme);
    }

    long timingStampOffset = 0;

    @Override
    protected void renderHandle() {
        Image image;



        //绘制判定线
        image = getImage(FixedOrbitObjectKey.PREVIEW_JUDGED_LINE);
        graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(mainJudgedLinePositionPercentage.doubleValue()) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

        //绘制底部线
        image = getImage(FixedOrbitObjectKey.BOTTOM_LINE);
        graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(0) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

        //绘制头部线
        image = getImage(FixedOrbitObjectKey.TOP_LINE);
        graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(getLastTime() + getRenderInfo().judgedLinePositionTimeOffset.get()) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());


    }


}
