package team.zxorg.mapeditcore.map;

import team.zxorg.mapeditcore.mapElement.IMapElement;

import java.util.ArrayList;

public class MapUtil {
    /**
     * 二分法搜索元素
     *
     * @param mapElements    要搜索的列表
     * @param noteTime 要搜索的时间
     * @return 小于等于元素时间的最大时间元素下标
     */
    public static int binarySearchMapElement(ArrayList<IMapElement> mapElements, int noteTime) {
        int min = 0;
        int max = mapElements.size() - 1;
        while (true) {
            int mid = (min + max) / 2;
            if (mapElements.get(mid).getTime() <= noteTime) {
                if (mapElements.size() == mid + 1) {
                    //到结尾了
                    return mid + 1;
                } else if (mapElements.get(mid + 1).getTime() > noteTime)
                    //中位小于等于搜索时间,且中位之后大于搜索时间,搜索结束
                    return mid + 1;
                else
                    //向后二分搜索
                    min = mid + 1;
            } else {
                if (mid == 0) {
                    //到头了
                    return mid;
                } else if (mapElements.get(mid).getTime() <= noteTime)
                    //中位大于等于搜索时间,且中位之前小于等于搜索时间,搜索结束
                    return mid;
                else
                    //向前二分搜索
                    max = mid;
            }
        }
    }
}
