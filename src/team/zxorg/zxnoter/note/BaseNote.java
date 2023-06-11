package team.zxorg.zxnoter.note;

public abstract class BaseNote {
    /**
     * 按键或按键头部时间戳
     */
    long timeStamp;

    public BaseNote(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
