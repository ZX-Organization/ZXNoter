package team.zxorg.zxnoter.ui_old.render.fixedorbit;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.ui_old.render.fixedorbit.key.FixedOrbitObjectKey;

public class FixedOrbitPreviewBackgroundRender extends FixedOrbitRender {
    public FloatProperty mainJudgedLinePositionPercentage = new SimpleFloatProperty();
    LongProperty mapTimeLength;//谱面时长

    public FixedOrbitPreviewBackgroundRender(FixedOrbitRenderInfo renderInfo, ZXMap renderZXMap, Canvas canvas, String theme, LongProperty mapTimeLength) {
        super(renderInfo, renderZXMap, canvas, theme);
        this.mapTimeLength = mapTimeLength;
    }

    long timingStampOffset = 0;

    @Override
    protected void renderHandle() {
        Image image;


        //绘制判定线
        image = getImage(FixedOrbitObjectKey.PREVIEW_JUDGED_LINE);
        graphics.drawImage(image, 0, getInfo().getTimeToPosition(mainJudgedLinePositionPercentage.doubleValue()) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

        //绘制底部线
        image = getImage(FixedOrbitObjectKey.BOTTOM_LINE);
        graphics.drawImage(image, 0, getInfo().getTimeToPosition(0) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());

        //绘制头部线
        image = getImage(FixedOrbitObjectKey.TOP_LINE);
        graphics.drawImage(image, 0, getInfo().getTimeToPosition(mapTimeLength.get() + getInfo().judgedLinePositionTimeOffset.get()) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());


    }


}
