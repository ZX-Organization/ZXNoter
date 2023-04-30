package com.github.zxorganization.test.fft2.reader;

import com.github.zxorganization.test.fft2.frame.FFTFrame;
import com.github.zxorganization.test.fft2.generator.FFTGenerator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class BufferedFFTReader extends FFTReader{
    LinkedHashMap<Long, FFTFrame> fftFrameCache = new LinkedHashMap<>();

    public BufferedFFTReader(FFTGenerator fftGenerator) {
        super(fftGenerator);
    }
}
