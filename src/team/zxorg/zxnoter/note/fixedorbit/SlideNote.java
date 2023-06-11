package team.zxorg.zxnoter.note.fixedorbit;

public class SlideNote extends FixedOrbitNote{
    /**
     * 滑键参数,正右负左
     * 值大小为滑动轨道数
     * 0参数不合法
     */
    int slideArg;
    public SlideNote(long timeStamp, int orbit, int slideArg) {
        super(timeStamp, orbit);
        this.slideArg = slideArg;
    }
}
