package com.github.zxorganization.test.fft2;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.DetermineDurationProcessor;
import be.tarsos.dsp.beatroot.BeatRootOnsetEventHandler;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import com.github.zxorganization.test.fft2.generator.FFTRealtimeGenerator;
import com.github.zxorganization.test.fft2.reader.FFTReader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.RandomAccess;

public class Test  {
    public static void main(String[] args)  throws InterruptedException {
        /*RandomAccessFile randomAccessFile=new RandomAccessFile(new File("testresources/ゆびきり.wav"),"r");

        AudioInputStream audio = AudioSystem.getAudioInputStream(new File("testresources/ゆびきり.wav"));
        AudioInputStream audioEncoding =AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, audio);


        FFTReader fftReader = new FFTReader(new FFTRealtimeGenerator(audio));*/
        AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromPipe("testresources/ゆびきり.wav", 44100, 2048, 0);
        DetermineDurationProcessor ddp = new DetermineDurationProcessor();
        audioDispatcher.addAudioProcessor(ddp);
        audioDispatcher.run();
        audioDispatcher.skip(50);


        File audioFile = new File("testresources/ゆびきり.wav");
        int size = 512;
        int overlap = 256;
        int sampleRate = 44100;
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(audioFile.getAbsolutePath(), sampleRate, size, overlap);

        ComplexOnsetDetector detector = new ComplexOnsetDetector(size);
        BeatRootOnsetEventHandler handler = new BeatRootOnsetEventHandler();
        detector.setHandler(handler);

        dispatcher.addAudioProcessor(detector);
        dispatcher.run();

        handler.trackBeats(this);




    }


    @Override
    public String name() {
        return "beat";
    }

    @Override
    public String description() {
        String descr = "\tCalculates onsets using a complex domain onset detector. " +
                "\n\tThe output is a semicolon separated list of a timestamp, and a salliance. ";
        descr += "\n\n\tinput.wav\t\ta readable wav file.";
        descr += "";
        return descr;
    }

    @Override
    public String synopsis() {
        String helpString = "input.wav";
        return helpString;
    }

}
