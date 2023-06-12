package team.zxorg.zxnoter.note.fixedorbit;

/**
 * 长键
 */
public class LongNote extends FixedOrbitNote implements Cloneable{
    /**
     *  持续时间
     */
    long sustainedTime;
    public LongNote(long timeStamp, int orbit , long sustainedTime) {
        super(timeStamp, orbit);
        this.sustainedTime = sustainedTime;
    }

    @Override
    public LongNote clone() {
        return new LongNote(timeStamp , orbit , sustainedTime);
    }

    @Override
    public String toString() {
        return '\n' +"LongNote{" +
                "持续时间=" + sustainedTime +
                ", 起始轨道=" + orbit +
                ", 起始时间=" + timeStamp +
                '}';
    }
}
