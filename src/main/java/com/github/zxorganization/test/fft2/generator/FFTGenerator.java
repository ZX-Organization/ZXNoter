package com.github.zxorganization.test.fft2.generator;

import com.github.zxorganization.test.fft2.frame.FFTFrame;

public interface FFTGenerator {
    FFTFrame getFrame(long t);
}
