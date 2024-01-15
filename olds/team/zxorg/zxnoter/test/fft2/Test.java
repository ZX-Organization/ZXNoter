package team.zxorg.zxnoter.test.fft2;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.beatroot.BeatRootOnsetEventHandler;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;

import java.io.*;

public class Test  {
    public static void main(String[] args)  throws InterruptedException {
        /*RandomAccessFile randomAccessFile=new RandomAccessFile(new File("testresources/ゆびきり.wav"),"r");

        AudioInputStream audio = AudioSystem.getAudioInputStream(new File("testresources/ゆびきり.wav"));
        AudioInputStream audioEncoding =AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, audio);


        FFTReader fftReader = new FFTReader(new FFTRealtimeGenerator(audio));*/
        /*AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromPipe("testresources/ゆびきり.wav", 44100, 2048, 0);
        DetermineDurationProcessor ddp = new DetermineDurationProcessor();
        audioDispatcher.addAudioProcessor(ddp);
        audioDispatcher.run();
        audioDispatcher.skip(50);*/


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

        OnsetHandler beatHandler= (v, v1) -> System.out.println(v);

        handler.trackBeats(beatHandler);

    }




}
