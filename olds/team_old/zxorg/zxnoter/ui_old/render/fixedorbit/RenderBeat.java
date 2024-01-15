package team.zxorg.zxnoter.ui_old.render.fixedorbit;

import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.BaseNote;
import team.zxorg.zxnoter.note.fixedorbit.ComplexNote;
import team.zxorg.zxnoter.note.fixedorbit.LongNote;
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
        this.measure = 1;
    }

    @Override
    public String toString() {
        return "RenderBeat{" +
                "time=" + time +
                ", measure=" + measure +
                ", isBase=" + isBase +
                '}';
    }



    public static ArrayList<Long> keyPoint(ArrayList<BaseNote> baseNotes, boolean andLongEnd) {
        ArrayList<Long> keyPoint = new ArrayList<Long>();//记录关键点
        for (BaseNote note : baseNotes) {
            keyPoint.add(note.timeStamp);
            if (note instanceof LongNote longNote && andLongEnd) {
                keyPoint.add(note.timeStamp + longNote.sustainedTime);
            }
        }
        return keyPoint;
    }


    /**
     * 获取最后的键位置  (可能的)
     *
     * @return 时间戳
     */
    public static long getZXMapLastTime(ZXMap zxMap) {
        long time = 0;
        if (zxMap.notes.size() - 1 >= 0) {
            BaseNote lastNote = zxMap.notes.get(zxMap.notes.size() - 1);
            if (lastNote instanceof ComplexNote complexNote) {
                time = complexNote.notes.get(complexNote.notes.size() - 1).timeStamp;
            } else if (lastNote instanceof LongNote longNote) {
                time = longNote.timeStamp + longNote.sustainedTime;
            } else {
                time = zxMap.notes.get(zxMap.notes.size() - 1).timeStamp;
            }
        }
        if (zxMap.timingPoints.size() > 1) {
            time = Math.max(zxMap.timingPoints.get(zxMap.timingPoints.size() - 1).timestamp, time);
        }
        return time;
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

    public static long alignBeatsTime(ArrayList<RenderBeat> renderBeats, long time) {
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
