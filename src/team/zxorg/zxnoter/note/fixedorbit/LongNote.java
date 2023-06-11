package team.zxorg.zxnoter.note.fixedorbit;

/**
 * 长键
 */
public class LongNote extends FixedOrbitNote{
    /**
     *  持续时间
     */
    long sustainedTime;
    public LongNote(long timeStamp, int orbit , long sustainedTime) {
        super(timeStamp, orbit);
        this.sustainedTime = sustainedTime;
    }
}
