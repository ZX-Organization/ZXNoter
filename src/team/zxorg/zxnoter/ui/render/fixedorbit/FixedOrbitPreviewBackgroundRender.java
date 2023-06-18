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


        //绘制拍线

        double beatCycleTime;
        long beatTime;


        for (long time = getRenderInfo().getPositionToTime(canvas.getHeight()); time < getRenderInfo().getPositionToTime(0); time++) {
            Timing timing = findAfterTiming(time);
            beatCycleTime = 60000. / (timing.absBpm);

            if (time < 0)
                continue;

            //拥有基准
            beatTime = time;

            //绘制拍
            if ((beatTime - timing.timingStamp) % beatCycleTime < 1) {
                image = getImage(FixedOrbitObjectKey.PREVIEW_BEAT_LINE);
                graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(beatTime) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
                //graphics.setFill(Color.WHEAT);
                //graphics.fillText("bt:" + beatCycleTime, 240, getRenderInfo().getTimeToPosition(beatTime));
            }

        }

        //绘制Timing线
        image = getImage(FixedOrbitObjectKey.TIMING_LINE);
        for (int i = 0; i < zxMap.timingPoints.size(); i++) {
            Timing timing = zxMap.timingPoints.get(i);
            graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(timing.timingStamp) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
        }


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

    /**
     * 寻找之后的Timing
     *
     * @return
     */
    public Timing findAfterTiming(long time) {
        //找到上一个timing
        long timingStampOffset = 0;
        Timing timing = new Timing(0, 1, false, 0);
        for (int i = 0; i < zxMap.timingPoints.size(); i++) {
            if (timing.isNewBaseBpm)
                timingStampOffset = timing.timingStamp;
            if (zxMap.timingPoints.get(i).timingStamp > time) {
                return timing;
            }
            timing = zxMap.timingPoints.get(i);
        }
        timing.timingStamp = timingStampOffset;
        return timing;
    }

}
