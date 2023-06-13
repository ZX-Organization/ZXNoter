package team.zxorg.zxnoter.note;

/**
 * 基本键
 */
public abstract class BaseNote {
    /**
     * 按键或按键头部时间戳
     */
    public long timeStamp;

    public BaseNote(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
