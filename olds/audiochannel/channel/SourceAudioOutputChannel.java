package team.zxorg.extensionloader.audiochannel.channel;

import javax.sound.sampled.*;

public class SourceAudioOutputChannel extends AudioOutputChannel {
    private final SourceDataLine sourceDataLine;


    public SourceAudioOutputChannel(SourceDataLine sourceDataLine, int bufferSize) {
        super(sourceDataLine.getFormat(), bufferSize);
        this.sourceDataLine = sourceDataLine;
    }

    public SourceAudioOutputChannel(AudioFormat audioFormat, int bufferSize) throws LineUnavailableException {
        super(audioFormat, bufferSize);
        sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
    }


    @Override
    public void writeBytes(byte[] data) {
        sourceDataLine.write(data, 0, data.length);
    }

    @Override
    public void open() {
        try {
            sourceDataLine.open(audioFormat, 2048 * 4);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        sourceDataLine.start();
    }

    @Override
    public void close() {
        sourceDataLine.stop();
        sourceDataLine.close();
    }


    public static SourceDataLine findDefineSourceDataLine() throws LineUnavailableException {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] sourceLineInfos = mixer.getSourceLineInfo();
            for (Line.Info lineInfo : sourceLineInfos) {
                if (lineInfo.getLineClass() == SourceDataLine.class) {
                    return (SourceDataLine) mixer.getLine(lineInfo);
                }
            }
        }
        throw new RuntimeException("No default SourceDataLine found.");
    }

}
