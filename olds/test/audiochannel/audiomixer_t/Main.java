package audiochannel.audiomixer_t;

import com.zedo.audiomixer_t.channel.AudioMemorySampleInputStream;
import com.zedo.audiomixer_t.channel.FixedLengthAudioSampleInputStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        Path audioPath = Path.of("testaudio/ゆびきり-2c-Lc.wav");
        //TargetDataLine line = AudioSystem.getTargetDataLine(new AudioFormat(44100, 8, 1, true, false));
        //System.out.println(line.getFormat());
        long t = System.currentTimeMillis();
        AudioMemorySampleInputStream audioFileChannel = AudioMemorySampleInputStream.build(audioPath);
        JFrame jFrame = new JFrame("666");
        jFrame.setSize(1920, 600);
        jFrame.show();
        Thread.sleep(100);
        Graphics g = jFrame.getGraphics();
        g.setColor(Color.GREEN);

        audioFileChannel.setTimePosition(5000);
        jFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 37 -> {

                    }
                    case 39 -> {

                    }
                }
                audioFileChannel.setTimePosition(10);
                g(audioFileChannel, g);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        new Thread(() -> {
            long time = 0;
            while (true) {
                time += 1;
                audioFileChannel.setTimePosition(time);
                g(audioFileChannel, g);
                try {
                    Thread.sleep(1000 / 120);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        //newInputStream.mark(50);
        //float[] buffer = new float[10];
        //newInputStream.read(buffer,0,10);

    }

    public static void g(FixedLengthAudioSampleInputStream audioFileChannel, Graphics g) {
        g.clearRect(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (int i = 0; i < 1920; i++) {
            int sampleMin = 0;
            int sampleMax = 0;
            int sample = 0;
            for (int j = 0; j < 800; j++) {
                sample = (int) (audioFileChannel.readFloatSample() * 300);
                sampleMin = Math.min(sample, sampleMin);
                sampleMax = Math.max(sample, sampleMax);
            }

            g.drawLine(i, 300 + sampleMax, i, 300 + sampleMin);
        }
    }

}