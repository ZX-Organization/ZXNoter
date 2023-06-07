package team.zxorg.zxnoter.test.fft2.generator;

import team.zxorg.zxnoter.test.fft2.frame.FFTFrame;

public interface FFTGenerator {
    FFTFrame getFrame(long t);
}
