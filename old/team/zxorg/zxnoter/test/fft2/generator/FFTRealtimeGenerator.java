package team.zxorg.zxnoter.test.fft2.generator;

import team.zxorg.zxnoter.test.fft2.frame.FFTFrame;

import javax.sound.sampled.AudioInputStream;

public class FFTRealtimeGenerator implements FFTGenerator {

    public FFTRealtimeGenerator(AudioInputStream audioInputStream) {

    }

    @Override
    public FFTFrame getFrame(long t) {
        return null;
    }
}
