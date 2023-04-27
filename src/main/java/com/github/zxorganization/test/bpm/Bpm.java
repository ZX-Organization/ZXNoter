package com.github.zxorganization.test.bpm;

import com.github.zxorganization.test.fft.FFT;

import java.net.URL;
import java.nio.file.Path;

public class Bpm {
    public static void main(String[] args) throws Exception {
        //URL audioURL = Bpm.class.getResource("ゆびきり_drums.wav");
        //FFT fft = new FFT(Path.of("audioFFT.fft"));
        //System.out.println(fft.getSamplingLength());
        //Application.launch(App.class);
        long t = System.currentTimeMillis();
        FFT fft = new FFT(Path.of("audioFFT.fft"), Path.of("ゆびきり_drums.wav"), 512, 64);
        System.out.println(System.currentTimeMillis() - t + "ms");
        //FFT.save(fft, Path.of("temp.json"));
        //int anticipation;//预期处理数 如果
        //int minCollectionRange;//最小采样区间
        //int expectedNumber;//期望数
        //FFT fft = new FFT(Path.of("temp.json"));

    }

    public static void fftToFile(double[][] fft) {

    }


}
