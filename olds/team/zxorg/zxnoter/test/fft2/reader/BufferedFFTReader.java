package team.zxorg.zxnoter.test.fft2.reader;

import team.zxorg.zxnoter.test.fft2.frame.FFTFrame;
import team.zxorg.zxnoter.test.fft2.generator.FFTGenerator;

import java.util.LinkedHashMap;

public class BufferedFFTReader extends FFTReader{
    LinkedHashMap<Long, FFTFrame> fftFrameCache = new LinkedHashMap<>();

    public BufferedFFTReader(FFTGenerator fftGenerator) {
        super(fftGenerator);
    }
}
