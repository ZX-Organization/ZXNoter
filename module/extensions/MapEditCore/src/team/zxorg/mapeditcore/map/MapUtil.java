package team.zxorg.mapeditcore.map;

import team.zxorg.mapeditcore.note.Note;
import team.zxorg.mapeditcore.timing.Timing;

import java.util.ArrayList;

public class MapUtil {
    /**
     * 二分法搜索物件时间
     *
     * @param notes    要搜索的列表
     * @param noteTime 要搜索的时间
     * @return 小于等于物件时间的最大时间物件下标
     */
    public static int binarySearchNoteTime(ArrayList<Note> notes, int noteTime) {
        int min = 0;
        int max = notes.size() - 1;
        while (true) {
            int mid = (min + max) / 2;
            if (notes.get(mid).getTime() <= noteTime) {
                if (notes.size() == mid + 1) {
                    //到结尾了
                    return mid + 1;
                } else if (notes.get(mid + 1).getTime() > noteTime)
                    //中位小于等于搜索时间,且中位之后大于搜索时间,搜索结束
                    return mid + 1;
                else
                    //向后二分搜索
                    min = mid + 1;
            } else {
                if (mid == 0) {
                    //到头了
                    return mid;
                } else if (notes.get(mid).getTime() <= noteTime)
                    //中位大于等于搜索时间,且中位之前小于等于搜索时间,搜索结束
                    return mid;
                else
                    //向前二分搜索
                    max = mid;
            }
        }
    }
    /**
     * 二分法搜索timing时间
     *
     * @param timings    要搜索的列表
     * @param timingTime 要搜索的时间
     * @return 小于等于物件时间的最大时间timing下标
     */
    public static int binarySearchTimingTime(ArrayList<Timing> timings, int timingTime) {
        int min = 0;
        int max = timings.size() - 1;
        while (true) {
            int mid = (min + max) / 2;
            if (timings.get(mid).getTime() <= timingTime) {
                if (timings.size() == mid + 1) {
                    //到结尾了
                    return mid + 1;
                } else if (timings.get(mid + 1).getTime() > timingTime)
                    //中位小于等于搜索时间,且中位之后大于搜索时间,搜索结束
                    return mid + 1;
                else
                    //向后二分搜索
                    min = mid + 1;
            } else {
                if (mid == 0) {
                    //到头了
                    return mid;
                } else if (timings.get(mid).getTime() <= timingTime)
                    //中位大于等于搜索时间,且中位之前小于等于搜索时间,搜索结束
                    return mid;
                else
                    //向前二分搜索
                    max = mid;
            }
        }
    }
}
