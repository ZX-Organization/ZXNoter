package com.github.zxorganization.test.fft2.reader;

import com.github.zxorganization.test.fft2.frame.FFTFrame;
import com.github.zxorganization.test.fft2.generator.FFTGenerator;

public class FFTReader {
    FFTGenerator fftGenerator;//帧生成器

    public FFTReader(FFTGenerator fftGenerator) {
        this.fftGenerator = fftGenerator;
    }

    public FFTFrame getFrame(long t) {
        return fftGenerator.getFrame(t);
    }
}
