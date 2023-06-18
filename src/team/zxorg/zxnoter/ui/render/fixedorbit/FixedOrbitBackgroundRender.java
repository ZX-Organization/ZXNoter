package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.timing.Timing;
import team.zxorg.zxnoter.ui.render.basis.RenderRectangle;
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
            graphics.drawImage(image, i * (renderInfo.canvasWidth.get() / orbits), 0, renderInfo.canvasWidth.get() / orbits, renderInfo.canvasHeight.get());
        }


        int subBeats = 12;


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

            //绘制分拍
            if ((beatTime - (timing.isNewBaseBpm ? timing.timingStamp : 0)) % (beatCycleTime / subBeats) < 1) {
                image = getImage(FixedOrbitObjectKey.SUB_BEAT_LINE);
                graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(beatTime) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
                graphics.setFill(Color.WHEAT);
                graphics.fillText(beatTime + "ms" + " t:" + Math.round(beatCycleTime) + " b:" + (timing.absBpm), 10, getRenderInfo().getTimeToPosition(beatTime));
            }

            //绘制拍
            if ((beatTime - (timing.isNewBaseBpm ? timing.timingStamp : 0)) % beatCycleTime < 1) {
                image = getImage(FixedOrbitObjectKey.BEAT_LINE);
                graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(beatTime) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
                //graphics.setFill(Color.WHEAT);
                //graphics.fillText("bt:" + beatCycleTime, 240, getRenderInfo().getTimeToPosition(beatTime));
            }

/*
            //绘制拍 （测试）
            if ((beatTime - timing.timingStamp) % beatCycleTime < 1) {
                image = getImage(FixedOrbitObjectKey.RED_LINE);
                graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(beatTime) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
            }*/

        }

        //绘制Timing线
        image = getImage(FixedOrbitObjectKey.TIMING_LINE);
        for (int i = 0; i < zxMap.timingPoints.size(); i++) {
            Timing timing = zxMap.timingPoints.get(i);
            graphics.drawImage(image, 0, getRenderInfo().getTimeToPosition(timing.timingStamp) - image.getHeight() / 2, renderInfo.canvasWidth.get(), image.getHeight());
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

        image = getImage(FixedOrbitObjectKey.IKUN);
        RenderRectangle rectangle = new RenderRectangle(image);

        rectangle.setRelativePosition(new RenderRectangle(canvas), Pos.CENTER);
        rectangle.offsetPositionByHalf(Pos.BOTTOM_LEFT);

        //rectangle.setRelativePosition(Pos.TOP_LEFT);
        drawImage(image, rectangle);
        //drawImageWithPosition(image, getWidth(), 100, Pos.BOTTOM_CENTER);
        //drawImageWithRelativePosition(image, 0, 0,100,100, Pos.TOP_LEFT);
        //System.out.println(bpm);

    }

    /**
     * 寻找之后的Timing
     *
     * @return
     */
    public Timing findAfterTiming(long time) {
        //找到上一个timing
        Timing timing = new Timing(0, 1, false, 0);
        for (int i = 0; i < zxMap.timingPoints.size(); i++) {
            if (zxMap.timingPoints.get(i).timingStamp > time) {
                return timing;
            }
            timing = zxMap.timingPoints.get(i);
        }
        return timing;
    }

}
