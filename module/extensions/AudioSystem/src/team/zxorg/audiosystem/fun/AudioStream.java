package team.zxorg.audiosystem.fun;

public interface AudioStream {
    int read(float[] buffer, int offset, int length);
    int getSampleRate();
    int getChannels();
    long getDuration();  // 返回流的总时长
}
