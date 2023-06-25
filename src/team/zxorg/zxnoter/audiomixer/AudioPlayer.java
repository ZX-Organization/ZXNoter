package team.zxorg.zxnoter.audiomixer;

import javax.sound.sampled.*;
import java.io.*;


public class AudioPlayer {
    AudioInputStream audioInputStream;      //音频流
    long audioInputStreamSize;              //音频流大小
    ByteArrayInputStream audioData;         //音频数据
    AudioFormat audioFormat;                //音频格式
    int bufferSize = 10240;                 //缓冲区大小
    SourceDataLine line;                    //源数据线
    Sonic sonic;                            //音频处理
    long audioLength;                       //音频长度
    long audioInputStreamPos;               //当前音频流的位置
    FloatControl volumeControl;             //音量控制器
    long playTime;                          //当前播放时间
    long updatePlayTimesTime;               //更新播放时间的时间
    Object playTimeLock = new Object();     //播放时间锁
    AudioPlayerEventCallBack apecb;         //播放器事件
    boolean normalSpeedMode = true;         //一般变速模式 true 变速变调 false 变速变调
    boolean timeSynchronize = false;        //时间同步
    boolean preview = false;              //预览一次
    CanPauseThread playThread = new CanPauseThread(new CanPauseThread.CanPauseThreadCallBack() {
        @Override
        public void loop() {
            {
                if (!isOpen()) {
                    playThread.pause(true);
                    return;
                }

                final byte inBuffer[] = new byte[bufferSize];
                final byte outBuffer[] = new byte[bufferSize];
                int numRead, numWritten;


                try {
                    if (preview) {
                        numRead = audioInputStream.read(inBuffer, 0, bufferSize);
                        audioInputStream.reset();
                        audioInputStream.skip(audioInputStreamPos);
                    } else {
                        numRead = audioInputStream.read(inBuffer, 0, bufferSize);
                        audioInputStreamPos += numRead;
                    }


                    if (timeSynchronize) {
                        timeSynchronize = false;
                        synchronizePlayTime();
                    }


                    //synchronizePlayTime();
                    if (numRead == 0) {
                        pause(true);
                        if (hasApecb())
                            apecb.AudioPlayerEvent(AudioPlayerEvent.stopEvent);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (numRead <= 0) {
                    sonic.flushStream();
                } else {
                    sonic.writeBytesToStream(inBuffer, numRead);
                }
                do {
                    numWritten = sonic.readBytesFromStream(outBuffer, bufferSize);
                    if (numWritten > 0) {
                        line.write(outBuffer, 0, numWritten);
                    }
                } while (numWritten > 0);


                if (preview) {
                    preview = false;
                    pause(true);
                }

            }
        }

        @Override
        public void threadEvent(CanPauseThread.CanPauseThreadEvent cpte) {
            switch (cpte) {
                case loop -> {
                    if (line != null)
                        line.start();
                }
                case pause -> {
                    if (line != null)
                        line.stop();
                }
            }
        }
    });

    {
        playThread.start();
    }


    public AudioPlayer() {
    }

    public AudioPlayer(int buffSize) {
        this.bufferSize = buffSize;
    }


    public void setPlaySpeed(float v) {
        if (isOpen()) {
            if (normalSpeedMode) {
                sonic.setRate(v);
                sonic.setSpeed(1f);
            } else {
                sonic.setSpeed(v);
                sonic.setRate(1f);
            }
            synchronizePlayTimeLater();
        }
    }


    public void setEventCallBack(AudioPlayerEventCallBack apecb) {
        this.apecb = apecb;
    }

    private boolean hasApecb() {
        return apecb != null;
    }

    public void stop() throws IOException {
        if (isOpen()) {
            close();
            playThread.interrupt();
        }
    }


    public boolean isOpen() {
        if (line == null)
            return false;
        return line.isOpen();
    }

    public long getAudioLength() {
        if (isOpen())
            return audioLength;
        return 0;
    }

    public boolean isPause() {
        return playThread.isPause();
    }

    public void close() throws IOException {
        if (isOpen()) {
            line.stop();
            line.close();
            playThread.pause(true);
            audioInputStream.close();
            audioData.close();
        }
        if (hasApecb())
            apecb.AudioPlayerEvent(AudioPlayerEvent.closeEvent);
    }

    public void open(InputStream inputStream) throws Exception {
        audioInputStream = AudioSystem.getAudioInputStream(inputStream);
        audioFormat = audioInputStream.getFormat();
        audioInputStreamSize = audioInputStream.getFrameLength() * audioFormat.getFrameSize();
        line = AudioSystem.getSourceDataLine(audioFormat);
        line.open(audioFormat, bufferSize);
        audioLength = (audioInputStream.getFrameLength() * (long) audioFormat.getFrameSize() * 1000) / (long) ((audioFormat.getSampleRate() * (audioFormat.getSampleSizeInBits() / 8) * audioFormat.getChannels()));
        sonic = new Sonic((int) audioFormat.getSampleRate(), audioFormat.getChannels());
        audioInputStreamPos = 0;
        //line.start();
        volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        if (hasApecb())
            apecb.AudioPlayerEvent(AudioPlayerEvent.openEvent);

    }

    public void open(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        audioData = new ByteArrayInputStream(fileInputStream.readAllBytes());
        open(audioData);
    }

    public void setVolume(float v) {
        if (isOpen())
            volumeControl.setValue((volumeControl.getMinimum() - volumeControl.getMaximum()) * (1f - v) + volumeControl.getMaximum());
    }

    public float getVolume() {
        if (isOpen())
            return 1f - (volumeControl.getValue() - volumeControl.getMaximum()) / (volumeControl.getMinimum() - volumeControl.getMaximum());
        return 0f;
    }

    //同步播放时间
    private void synchronizePlayTimeLater() {
        timeSynchronize = true;
    }

    private void synchronizePlayTime() {
        synchronized (playTimeLock) {
            //计算时间
            updatePlayTimesTime = System.currentTimeMillis();
            playTime = ((long) ((double) audioInputStreamPos / audioInputStreamSize * audioLength));
        }
    }


    public void pause(boolean isPause) {
        if (isOpen()) {
            if (isPause != isPause()) {
                synchronizePlayTime();
                playThread.pause(isPause);

            }
            if (hasApecb()) {
                if (isPause)
                    apecb.AudioPlayerEvent(AudioPlayerEvent.pauseEvent);
                else
                    apecb.AudioPlayerEvent(AudioPlayerEvent.playEvent);
            }
        }


    }


    public void skip(long startTime) throws IOException {
        if (isOpen()) {
            audioInputStream.reset();
            audioInputStreamPos = (long) (audioInputStreamSize * ((double) startTime / (double) audioLength));
            audioInputStream.skip(audioInputStreamPos);
            synchronizePlayTime();

            if (hasApecb())
                apecb.AudioPlayerEvent(AudioPlayerEvent.skipEvent);
        }
    }

    public boolean isPreview() {
        return preview;
    }

    public void preview() {
        if (!preview) {
            preview = true;
            if (isPause())
                playThread.pause(false);
        }


    }

    public long getTime() {
        //System.out.println(line.getMicrosecondPosition()/1000);
        //System.out.println(((long) ((double) audioInputStreamPos / audioInputStreamSize * audioLength)));
        if (isOpen()) {
            long nowTime = System.currentTimeMillis();
            if (isPause() || preview)
                return playTime;
            return playTime + (long) ((nowTime - updatePlayTimesTime) * (normalSpeedMode ? sonic.getRate() : sonic.getSpeed()));


        }
        return 0L;
    }


    public interface AudioPlayerEventCallBack {
        void AudioPlayerEvent(AudioPlayerEvent ape);
    }

    public enum AudioPlayerEvent {
        openEvent, closeEvent, playEvent, pauseEvent, skipEvent, sonicEvent, stopEvent
    }


    public void setNormalSpeedMode(boolean normalSpeedMode) {
        this.normalSpeedMode = normalSpeedMode;
    }
}
