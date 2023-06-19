package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.ui.render.RenderInfo;

import java.util.concurrent.Callable;

public class FixedOrbitRenderInfo extends RenderInfo {


    /**
     * 判定线偏移百分比
     */
    public FloatProperty judgedLinePositionPercentage = new SimpleFloatProperty(0.95f);
    /**
     * 判定线偏移时间 (只读)
     */
    public LongBinding judgedLinePositionTimeOffset = Bindings.createLongBinding(() -> (long) (canvasHeight.get() * judgedLinePositionPercentage.get() / timelineZoom.get()),
            canvasHeight, judgedLinePositionPercentage, timelineZoom);

    public DoubleBinding orbitWidth = Bindings.createDoubleBinding(() -> canvasWidth.get() / orbits.get());

    public FixedOrbitRenderInfo() {
    }

    /**
     * 获取时间到y位置
     *
     * @param time
     * @return
     */
    public double getTimeToPosition(double time) {
        //判定线时间偏移
        double judgedLineTimeOffset = getJudgedLineTimeOffset();
        return (timelinePosition.get() - time + judgedLineTimeOffset) * timelineZoom.get();
    }

    /**
     * 获取判断线时间偏移位置
     *
     * @return 偏移时间
     */

    public double getJudgedLineTimeOffset() {
        return (canvasHeight.get() * judgedLinePositionPercentage.get() / timelineZoom.get());
    }

    /**
     * 获取y位置到时间
     *
     * @param y
     * @return
     */
    public long getPositionToTime(double y) {
        long topTime = (long) (timelinePosition.get() - (canvasHeight.get() - canvasHeight.get() * judgedLinePositionPercentage.get()) / timelineZoom.get());
        return (long) (topTime + (canvasHeight.get() - y) / timelineZoom.get());
    }

}
