package team.zxorg.mapeditcore.io.msc;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;

public class PlayThread extends Thread{
    /**
     * 源数据行
     */
    SourceDataLine dataLine;
    /**
     * 音频缓存
     */
    ByteBuffer musicBuffer;

    /**
     * 暂停标识
     */
    boolean pause = true;

    /**
     * 源数据控制器
     */
    FloatControl controller;

    public PlayThread(SourceDataLine dataLine, ByteBuffer musicBuffer) {
        this.dataLine = dataLine;
        this.musicBuffer = musicBuffer;
        controller = (FloatControl) dataLine.getControl(FloatControl.Type.MASTER_GAIN);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (!pause) {
                byte[] temp = new byte[256];
                musicBuffer.get(temp);
                dataLine.write(temp, 0, 256);
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pause(){
        pause = true;
    }
    /**
     * 继续播放
     */
    public void cancelPause(){
        pause = false;
    }

    /**
     * 设置播放音量
     */
    public void setVolume(float volume){
        controller.setValue(volume);
    }
    /**
     * 设置播放位置
     * @param playPosition 播放位置(>=0,<=1);
     */
    public void setPlayPosition(double playPosition){
        if (playPosition>=0&&playPosition<=1)
            musicBuffer.position((int) (musicBuffer.capacity() * playPosition));
        else System.out.println("播放时间非法");
    }
}
