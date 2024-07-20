package team.zxorg.mapeditcore.mapElement;

/**
 * 谱面元素，包括物件和timing（以后可能有其他元素如关键帧,实现此接口）
 * 提供时间位置的获取方式
 * 比较器需专门实现
 */
public interface IMapElement extends Comparable<IMapElement>{
    int getTime();
    default IMapElement clone(){return this;};

}
