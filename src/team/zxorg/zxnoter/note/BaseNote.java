package team.zxorg.zxnoter.note;

/**
 * 基本键
 */
public abstract class BaseNote implements Cloneable,Comparable<BaseNote>{
    /**
     * 按键或按键头部时间戳
     */
    public long timeStamp;

    public BaseNote(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public abstract BaseNote clone();
    public abstract int getOrbit();
    public abstract void setOrbit(int orbit);
    public abstract int getImdNoteType();
    public abstract long getLength();
    @Override
    public int compareTo(BaseNote o) {
        if (timeStamp<o.timeStamp) return -1;
        else if (timeStamp>o.timeStamp) return 1;
        else return 0;
    }
}
