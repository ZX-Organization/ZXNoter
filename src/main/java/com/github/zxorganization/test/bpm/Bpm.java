package com.github.zxorganization.test.bpm;

import com.github.zxorganization.test.fft.FFT;
import javafx.application.Application;

import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;

public class Bpm {
    public static FFT fft;

    public static void main(String[] args) throws Exception {
        //URL audioURL = Bpm.class.getResource("ゆびきり_drums.wav");
        fft = new FFT(Path.of("audioFFT.fft"), Path.of("testresources/ゆびきり_drums_01.wav"), 64, 2);


        //fft = new FFT(Path.of("audioFFT.fft"));
        /*System.out.println(fft.getSampleLength());
        System.out.println(fft.getFFTAccuracy());
        System.out.println(fft.getAudioSampleRate());
        System.out.println(fft.fftTimeToPosition(100));
        System.out.println(Arrays.toString(fft.fftGet(55000 / 40)));
        System.out.println(Arrays.toString(fft.fftGet(65000 / 40)));*/


        Application.launch(App.class);
        //long t = System.currentTimeMillis();

        //System.out.println(System.currentTimeMillis() - t + "ms");
        //FFT.save(fft, Path.of("temp.json"));
        //int anticipation;//预期处理数 如果
        //int minCollectionRange;//最小采样区间
        //int expectedNumber;//期望数
        //FFT fft = new FFT(Path.of("temp.json"));

    }


}
