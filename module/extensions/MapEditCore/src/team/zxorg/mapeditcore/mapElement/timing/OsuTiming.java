package team.zxorg.mapeditcore.mapElement.timing;

import team.zxorg.mapeditcore.mapElement.IMapElement;

/**
 *      * 例子
 *      * 10000,333.33,4,0,0,100,1,1
 *      * 12000,-25,4,3,0,100,0,1
 *      * 10 秒处的第一个时间点为非继承时间点（红线），并包含以下的设置：
 *      *
 *      * BPM 为 180（1 / 333.33 * 1000 * 60）
 *      * 拍子记号为 4/4
 *      * 沿用谱面的默认音效组
 *      * 使用 osu! 的默认音效
 *      * 音量 100%
 *      * 开启 Kiai 时间
 *      * 12 秒处的第二个时间点为继承时间点（绿线），改变滑条速度为 4 倍，并将这一段的音效组切换成 drum 音效组。
 */
public class OsuTiming extends Timing{
    private int beats;
    private int hitSound;
    private int hitSoundPar;
    private int volume;
    private boolean isExtendTiming;
    private int effect;
    public OsuTiming(int time, double bpm, double speed, int beats, int hitSound,int hitSoundPar, int volume, boolean isExtendTiming, int effect) {
        super(time, bpm, speed);
        type = "OsuTiming";
        setBeats(beats);
        setHitSound(hitSound);
        setHitSoundPar(hitSoundPar);
        setVolume(volume);
        setExtendTiming(isExtendTiming);
        setEffect(effect);
    }

    /**
     * 节拍（整型）： 一小节中的拍子数量。继承时间点（绿线）的这个值无效果。
     */
    public int getBeats() {
        return beats;
    }

    /**
     * 设置一小节中的拍子数量
     */
    public void setBeats(int beats) {
        this.beats = beats;
    }

    /**
     * 音效组（整型）： 物件使用的默认音效组（0 = 谱面默认设置（SampleSet），1 = normal，2 = soft，3 = drum）。
     */
    public int getHitSound() {
        return hitSound;
    }

    /**
     * 设置音效组
     */
    public void setHitSound(int hitSound) {
        this.hitSound = hitSound;
    }

    /**
     * 音效参数（整型）： 物件使用的自定义音效参数。 0 表示使用 osu! 默认的音效。
     */
    public int getHitSoundPar() {
        return hitSoundPar;
    }

    /**
     * 获取音效参数
     */
    public void setHitSoundPar(int hitSoundPar) {
        this.hitSoundPar = hitSoundPar;
    }

    /**
     * 音量（整型）： 击打物件的音量（0 - 100）。
     */
    public int getVolume() {
        return volume;
    }

    /**
     * 设置击打物件的音量
     * @param volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    /**
     * 是否为非继承时间点（红线）（布尔值）： 字面意思。
     */
    public boolean isExtendTiming() {
        return isExtendTiming;
    }

    /**
     * 设置是否继承时间点(红线)
     */
    public void setExtendTiming(boolean extendTiming) {
        isExtendTiming = extendTiming;
    }

    /**
     * 效果（整型）： 一位影响时间点特殊效果的参数。
     */
    public int getEffect() {
        return effect;
    }

    /**
     * 效果
     * 时间点可以通过在效果（整型）参数中，通过修改参数值为 1 或者 8（二进制下，是第 0 位和第 3 位），来开启两种特殊效果。
     *
     * 1（二进制的第 0 位）：是否开启 Kiai 时间
     * 8（二进制的第 3 位）：是否在 osu!taiko 和 osu!mania 模式下，忽略红线的第一条小节线显示
     * 若想同时开启两种效果，则可填 9（1 + 8 = 9）。其余的二进制位暂不使用。
     */
    public void setEffect(int effect) {
        this.effect = effect;
    }
    @Override
    public String toString() {
        return '\n'+"OsuTiming{" +
                "time=" + time +
                ", bpm=" + bpm +
                ", speed=" + speed +
                ", beats=" + beats +
                ", hitSound=" + hitSound +
                ", hitSoundPar=" + hitSoundPar +
                ", volume=" + volume +
                ", isExtendTiming=" + isExtendTiming +
                ", effect=" + effect +
                '}';
    }
}
