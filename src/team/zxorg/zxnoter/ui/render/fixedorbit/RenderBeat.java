package team.zxorg.zxnoter.ui.render.fixedorbit;

import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.timing.Timing;

import java.util.ArrayList;

public class RenderBeat {
    public long time;//时间戳
    public Timing timing;//所属Timing
    public int measure;//分拍数
    public boolean isBase;//是基准

    public RenderBeat(long time, Timing timing, boolean isBase) {
        this.time = time;
        this.timing = timing;
        this.isBase = isBase;
    }

    @Override
    public String toString() {
        return "RenderBeat{" +
                "time=" + time +
                ", measure=" + measure +
                ", isBase=" + isBase +
                '}';
    }

    public static void upDateBeats(ZXMap zxMap, ArrayList<RenderBeat> renderBeats) {

        renderBeats.clear();//清空
        //之前的基准
        Timing previousBaseTiming = null;
        //现在的基准
        Timing nowBaseTiming;
        //遍历用
        long time = 0;//当前处理的时间

        //遍历所以Timing
        for (Timing timing : zxMap.timingPoints) {
            //记录新基准
            if (timing.isNewBaseBpm) {
                nowBaseTiming = timing;
                //计算 之前的和现在 中间节拍
                if (previousBaseTiming != null) {
                    //之前的基准BPM时间
                    double beatCycleTime = 60000. / (previousBaseTiming.absBpm);
                    int counts = (int) ((double) (nowBaseTiming.timestamp - previousBaseTiming.timestamp) / beatCycleTime);
                    //System.out.println(counts);
                    for (int i = 0; i < counts; i++) {
                        renderBeats.add(new RenderBeat(previousBaseTiming.timestamp + (long) (i * beatCycleTime), previousBaseTiming, (i == 0)));
                    }
                }
                //赋值之前基准
                previousBaseTiming = nowBaseTiming;
            }

        }

    }

    public static RenderBeat findTime(ArrayList<RenderBeat> renderBeats, long time) {
        //遍历所以Timing
        for (int i = 0; i < renderBeats.size(); i++) {
            if (renderBeats.get(i).time > time) {
                if (i - 1 < 0)
                    return null;
                return renderBeats.get(i - 1);
            }
        }
        return null;
    }

    public static long alignBeatsTime(ArrayList<RenderBeat> renderBeats,long time) {
        long closestTime = Long.MAX_VALUE; // 最接近时间的初始值设为最大值
        for (RenderBeat renderBeat : renderBeats) {
            double beatCycleTime = 60000.0 / renderBeat.timing.absBpm;
            long t;

            // 节拍
            t = renderBeat.time;

            // 找到最近节拍，然后再去分拍里找到最近的分拍

            // 分拍
            for (int i = 0; i < renderBeat.measure; i++) {
                t = (long) (renderBeat.time + (beatCycleTime / renderBeat.measure) * i);
                // 判断 t 是否更接近给定时间 time
                if (Math.abs(t - time) < Math.abs(closestTime - time)) {
                    closestTime = t; // 更新最接近时间
                }
            }

            // 判断 t 是否更接近给定时间 time
            if (Math.abs(t - time) < Math.abs(closestTime - time)) {
                closestTime = t; // 更新最接近时间
            }
        }

        return closestTime; // 返回最接近时间
    }
}
