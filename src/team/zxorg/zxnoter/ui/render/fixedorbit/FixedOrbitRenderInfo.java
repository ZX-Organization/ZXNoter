package team.zxorg.zxnoter.ui.render.fixedorbit;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleFloatProperty;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
import team.zxorg.zxnoter.ui.render.RenderInfo;

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

    public FixedOrbitRenderInfo() {
    }

    /**
     * 获取时间到y位置
     *
     * @param time
     * @return
     */
    public double getTimeToPosition(long time) {
        //判定线时间偏移
        long judgedLineTimeOffset = getJudgedLineTimeOffset();
        return (timelinePosition.get() - time + judgedLineTimeOffset) * timelineZoom.get();
    }

    /**
     * 获取判断线时间偏移位置
     *
     * @return 偏移时间
     */

    public long getJudgedLineTimeOffset() {
        return (long) (canvasHeight.get() * judgedLinePositionPercentage.get() / timelineZoom.get());
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
