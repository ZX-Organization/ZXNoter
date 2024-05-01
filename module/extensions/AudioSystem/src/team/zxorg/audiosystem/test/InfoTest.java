package team.zxorg.audiosystem.test;

import javax.sound.sampled.*;

public class InfoTest {
    public static void main(String[] args) {
        // 获取所有音频设备信息
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        // 遍历每个音频设备信息
        for (Mixer.Info mixerInfo : mixerInfos) {
            System.out.println("Audio Device: " + mixerInfo.getName());
            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            // 获取音频设备支持的行（Line）信息
            Line.Info[] lineInfos = mixer.getSourceLineInfo();

            // 遍历每个行信息
            for (Line.Info lineInfo : lineInfos) {
                if (lineInfo instanceof DataLine.Info) {
                    DataLine.Info dataLineInfo = (DataLine.Info) lineInfo;
                    AudioFormat[] formats = dataLineInfo.getFormats();

                    // 遍历每种格式
                    for (AudioFormat format : formats) {
                        System.out.println("    Supported Format: " + format.toString());
                    }
                }
            }
            System.out.println();
        }
    }
}
