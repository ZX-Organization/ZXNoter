package team.zxorg.mapeditcore.io.msc;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class WavPlayer {
    /**
     * wav源
     */
    WavSource wav;
    /**
     * 播放线程
     */
    private final PlayThread playThread;

    public WavPlayer(File file) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (!file.getName().endsWith(".wav"))
            throw new RuntimeException("不支持此格式音频文件：" + file.getName());
        wav = new WavSource();
        playThread = wav.newPlayThread(file);
    }

    /**
     * 设置播放时间（ms）
     * @param millisecond 毫秒
     */
    public void setTime(long millisecond){
        playThread.setPlayPosition(
                ((double) millisecond)/wav.audioLength
        );
    }

    /**
     * 暂停播放
     */
    public void pause(){
        playThread.pause();
    }

    /**
     * 继续播放
     */
    public void start(){
        playThread.cancelPause();
    }

    /**
     * 播放音量设置
     */
    public void setVolume(float volume){
        playThread.setVolume(volume);
    }

    public void stop() {
        playThread.interrupt();
    }
}
