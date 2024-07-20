package team.zxorg.mapeditcore.mapElement.note;

import java.io.File;
import java.nio.file.Path;

public class OsuNote extends Note{
    //x 与长键所在的键位有关。算法为：floor(x * 键位总数 / 512)，并限制在 0 和 键位总数 - 1 之间。
    /**
     * osu中定义物件类型的参数,1为单键,128为长条
     */
    private int keyType;
    /**
     *音效数据用于标记物件的打击音效：
     * 0：Normal（普通）
     * 2：Whistle（口哨）
     * 4：Finish（镲）
     * 8：Clap（鼓掌）
     * 如果没有设置打击音效，则默认为 Normal。
     */
    private int hitSound;
    /**
     * 普通音效组（整型）： Normal 音效的音效组
     */
    private int normalSampleSet;
    /**
     * 附加音效组（整型）： Whistle、Finish、Clap 音效的音效组
     */
    private int extendSampleSet;
    /**
     * 音效组编号（整型）： 用于区分不同音效组的编号。如果是 0，则表示这个打击物件的音效组，沿用控制该物件时间点的音效组设定。
     */
    private int sampleIndex;
    /**
     * 音量（整型）： 音效组的音量。如果是 0，则表示这个打击物件的音量，沿用控制该物件时间点的音量设定。
     */
    private int sampleVolume;
    /**
     * 文件名（字符串）： 自定义附加音效的文件名或相对路径。
     */
    protected Path soundFile;

    public OsuNote(int time, int orbit, int maxOrbit, int keyType, int hitSound, String[] sampleSetPars) {
        super(time, calPosByOrbit(orbit,maxOrbit));
        setKeyType(keyType);
        setHitSound(hitSound);
        setNormalSampleSet(Integer.parseInt(sampleSetPars[0]));
        setExtendSampleSet(Integer.parseInt(sampleSetPars[1]));
        setSampleIndex(Integer.parseInt(sampleSetPars[2]));
        setSampleVolume(Integer.parseInt(sampleSetPars[3]));
        if (sampleSetPars.length>4){
            setSoundFile(Path.of(new File(sampleSetPars[4]).toURI()));
        }
    }

    /**
     * osu中定义物件类型的参数,1为单键,128为长条
     */
    public int getKeyType() {
        return keyType;
    }

    /**
     * 设置此osu物件的类型
     */
    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    /**
     *音效数据用于标记物件的打击音效：
     * 0：Normal（普通）
     * 2：Whistle（口哨）
     * 4：Finish（镲）
     * 8：Clap（鼓掌）
     */
    public int getHitSound() {
        return hitSound;
    }

    /**
     * 如果没有设置打击音效，则默认为 Normal。
     */
    public void setHitSound(int hitSound) {
        this.hitSound = hitSound;
    }

    /**
     * 普通音效组（整型）： Normal 音效的音效组
     */
    public int getNormalSampleSet() {
        return normalSampleSet;
    }

    /**
     * 0：未设置自定义音效组
     * 对于普通打击音效，音效组由控制该物件的时间点决定。
     * 对于附加打击音效， 音效组由普通打击音效组决定。
     * 1：Normal 组
     * 2：Soft 组
     * 3：Drum 组
     */
    public void setNormalSampleSet(int normalSampleSet) {
        this.normalSampleSet = normalSampleSet;
    }

    /**
     * 附加音效组（整型）： Whistle、Finish、Clap 音效的音效组
     */
    public int getExtendSampleSet() {
        return extendSampleSet;
    }
    /**
     * 0：未设置自定义音效组
     * 对于普通打击音效，音效组由控制该物件的时间点决定。
     * 对于附加打击音效， 音效组由普通打击音效组决定。
     * 1：Normal 组
     * 2：Soft 组
     * 3：Drum 组
     */
    public void setExtendSampleSet(int extendSampleSet) {
        this.extendSampleSet = extendSampleSet;
    }

    /**
     * 音效组编号（整型）： 用于区分不同音效组的编号。如果是 0，则表示这个打击物件的音效组，沿用控制该物件时间点的音效组设定。
     *音效组编号与上述段落的音效组编号相同。如果值为0或1，则可忽略不写
     * 当加载谱面时，将会自动按从上到下的优先级加载对应名称的音效。
     * 当音效组编号不设为0时，加载谱面内的音效
     * 当无法在谱面内找到该音效组编号对应的文件时，加载玩家皮肤内的音效
     * 当无法在玩家皮肤内找到该音效组编号对应的文件时，加载 默认的音效
     */
    public int getSampleIndex() {
        return sampleIndex;
    }

    /**
     *音效组编号与上述段落的音效组编号相同。如果值为0或1，则可忽略不写
     * 当加载谱面时，将会自动按从上到下的优先级加载对应名称的音效。
     * 当音效组编号不设为0时，加载谱面内的音效
     * 当无法在谱面内找到该音效组编号对应的文件时，加载玩家皮肤内的音效
     * 当无法在玩家皮肤内找到该音效组编号对应的文件时，加载默认的音效
     */
    public void setSampleIndex(int sampleIndex) {
        this.sampleIndex = sampleIndex;
    }

    /**
     * 音量（整型）： 音效组的音量。如果是 0，则表示这个打击物件的音量，沿用控制该物件时间点的音量设定。
     */
    public int getSampleVolume() {
        return sampleVolume;
    }

    /**
     * 设置该物件音效组的音量
     */
    public void setSampleVolume(int sampleVolume) {
        this.sampleVolume = sampleVolume;
    }

    /**
     * 文件名（字符串）： 自定义附加音效的文件名或相对路径。
     */
    public Path getSoundFile() {
        return soundFile;
    }

    /**
     *当填写了文件名，此时会将这个文件替换掉物件默认的附加打击音效。
     */
    public void setSoundFile(Path soundFile) {
        this.soundFile = soundFile;
    }
    /**
     *当填写了文件名，此时会将这个文件替换掉物件默认的附加打击音效。
     */
    public void setSoundFile(String soundFile) {
        this.soundFile = Path.of(new File(soundFile).toURI());
    }

    @Override
    public String toString() {
        return '\n'+"      "+"OsuNote{" +
                "time=" + time +
                ", position=" + position +
                ", keyType=" + keyType +
                ", hitSound=" + hitSound +
                ", normalSampleSet=" + normalSampleSet +
                ", extendSampleSet=" + extendSampleSet +
                ", sampleIndex=" + sampleIndex +
                ", sampleVolume=" + sampleVolume +
                ", soundFile=" + soundFile +
                ", keyAudio=" + keyAudioPath +
                '}';
    }
}
