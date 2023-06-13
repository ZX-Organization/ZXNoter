package team.zxorg.zxnoter.note.fixedorbit;

/**
 * 滑键
 */
public class SlideNote extends FixedOrbitNote implements Cloneable{
    /**
     * 滑键参数,正右负左
     * 值大小为滑动轨道数
     * 0参数不合法
     */
    public int slideArg;
    public SlideNote(long timeStamp, int orbit, int slideArg) {
        super(timeStamp, orbit);
        this.slideArg = slideArg;
    }

    @Override
    public SlideNote clone(){
        return new SlideNote(timeStamp , orbit , slideArg);
    }

    @Override
    public String toString() {
        return '\n' +"SlideNote{" +
                "滑动=" + slideArg +
                ", 起始轨道=" + orbit +
                ", 起始时间=" + timeStamp +
                '}';
    }
}
