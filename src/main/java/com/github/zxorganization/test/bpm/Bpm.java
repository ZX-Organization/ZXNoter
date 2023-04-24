package com.github.zxorganization.test.bpm;

import com.github.zxorganization.sound.audiomixer.AudioChannel;
import com.github.zxorganization.test.bpm.App;
import com.github.zxorganization.test.fft.Complex;
import com.github.zxorganization.test.fft.FFT;
import javafx.application.Application;
import javafx.scene.paint.Color;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.Arrays;

public class Bpm {
    public static void main(String[] args) throws Exception {
        URL audioURL = App.class.getResource("../ゆびきり_drums.wav");

        //FFT fft = new FFT(Path.of("audioFFT.fft"));
        //System.out.println(fft.getSamplingLength());
        //Application.launch(App.class);
        long t = System.currentTimeMillis();
        FFT fft = new FFT(Path.of("audioFFT.fft"), Path.of(audioURL.toURI()), 512, 64);
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
