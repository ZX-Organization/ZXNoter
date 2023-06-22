package audiochannel;

import javax.swing.*;
import java.awt.*;

public class AudioFrame {
    JFrame jFrame = new JFrame("波形显示");
    public int[] showData;//显示采样
    public int[] buf;
    int zoom = 1;
    int size = 0;
    public float multiply=1;

    public AudioFrame(int size) throws HeadlessException {
        this.size = size;
        setZoom(1);

        jFrame.setSize(showData.length, 800);
        jFrame.show();
        jFrame.setBackground(Color.BLACK);
        Graphics g = jFrame.getGraphics();
        g.setColor(Color.GREEN);

        new Thread(() -> {
            while (true) {
                g.clearRect(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
                synchronized (this) {

                    int subZoom = 2;
                    float sampleL = 0;

                    for (int i = 0; i < size / subZoom; i++) {

                        float sample = (float) showData[(showData.length - size / subZoom)+ i] / (float) Integer.MAX_VALUE;

                        sample *= multiply;//增益


                        g.drawLine(i* subZoom, 200 + (int) (sampleL * 200), (i+1) * subZoom, 200 + (int) (sample * 200));
                        if (i > 1)
                            sampleL = sample;
                    }


                    for (int i = 0; i < size; i++) {
                        float sample = 0;

                        float sampleMin = 0;
                        float sampleMax = 0;

                        for (int j = 0; j < zoom; j++) {
                            sample = (float) showData[i * zoom+j] / (float) Integer.MAX_VALUE;
                            sample *= multiply;//增益

                            sampleMin = Math.min(sample, sampleMin);
                            sampleMax = Math.max(sample, sampleMax);
                        }


                        g.drawLine(i, 600 + (int) (sampleMin * 200), i, 600 + (int) (sampleMax * 200));


                    }
                }


                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    public void pushBuf() {
        synchronized (this) {
            for (int i = 0; i < showData.length - zoom; i++) {
                showData[i] = showData[i + zoom];
            }
            for (int i = 0; i < zoom; i++) {
                showData[showData.length - i - 1] = buf[i];
            }
        }

    }

    public void setZoom(int zoom) {
        synchronized (this) {
            this.zoom = zoom;
            showData = new int[size * zoom];
            buf = new int[zoom];
        }
    }

}
