package com.github.zxorganization.test.fft;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FFT {
    protected RandomAccessFile randomAccessFile;//打开的FFT文件

    public int getFFTAccuracy() {
        return fftAccuracy;
    }

    public int getSamplingAccuracy() {
        return samplingAccuracy;
    }

    public int getSamplingLength() {
        return samplingLength;
    }

    protected FileChannel fileChannel;//FFT文件通道
    protected MappedByteBuffer byteBuffer;//FFT文件字节缓冲区


    protected long byteBufferPosition;//字节缓冲区位置
    protected final int byteBufferSize = 409600;//字节缓冲区大小

    protected final int fftBufferSize = 512;//FFT缓冲区大小
    protected int fftPosition;//FFT缓冲区位置
    protected float[][] fftData;//FFT缓冲区

    protected int fftAccuracy;//fft精度
    protected int samplingAccuracy;//采样精度
    protected int samplingLength;//采样长度

    protected void putFloat(float f) throws IOException {
        check(4);
        //System.out.println("put");
        byteBuffer.putFloat(f);
    }

    protected float getFloat() throws IOException {
        check(4);
        return byteBuffer.getFloat();
    }

    protected void putInt(int i) throws IOException {
        check(4);
        //System.out.println("put");
        byteBuffer.putInt(i);
    }

    protected int getInt() throws IOException {
        check(4);
        return byteBuffer.getInt();
    }

    protected void putLong(long l) throws IOException {
        check(8);
        byteBuffer.putLong(l);
    }

    protected long getLong() throws IOException {
        check(4);
        return byteBuffer.getLong();
    }

    /**
     * 获取当前的位置
     *
     * @return
     */
    protected long position() {
        return byteBufferPosition + byteBuffer.position();
    }

    /**
     * 设置读取的位置
     *
     * @param newPosition 新的位置
     * @throws IOException
     */
    protected void position(long newPosition) throws IOException {
        //现位置起始和新位置的 距离
        long gap = newPosition - byteBufferPosition;
        if (gap > byteBuffer.capacity() || gap < 0) {//在右边 在左边
            //重新分配
            byteBuffer.clear();
            byteBufferPosition = newPosition;
            byteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, newPosition, byteBufferSize);
        } else {//在里面
            byteBuffer.position((int) (newPosition - byteBufferPosition));
        }

    }

    /**
     * 检查读取是否在缓冲区内
     *
     * @param size 需要读取的字节数
     * @throws IOException
     */
    protected void check(int size) throws IOException {
        int outByteSize = (byteBuffer.position() + size) - byteBufferSize;
        if (outByteSize > 0) {
            byteBuffer.clear();
            byteBufferPosition = byteBufferPosition + byteBufferSize - (size - outByteSize);
            byteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, byteBufferPosition, byteBufferSize);
        }
    }

    /**
     * 打开文件
     *
     * @param fftPath 文件目录
     * @param rewrite 重写
     * @throws IOException
     */
    protected void openFFTFile(Path fftPath, boolean rewrite) throws IOException {
        randomAccessFile = new RandomAccessFile(fftPath.toFile(), "rw");
        if (rewrite)
            randomAccessFile.setLength(0);
        fileChannel = randomAccessFile.getChannel();
        byteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, byteBufferSize);
    }

    public FFT(Path fftPath) throws IOException {
        openFFTFile(fftPath, false);
        this.fftAccuracy = getInt();//fft精度
        this.samplingAccuracy = getInt();//采样精度
        this.samplingLength = getInt();//采样长度
    }

    public FFT(Path fftPath, Path audioURL, int fftAccuracy, int samplingAccuracy) throws IOException, UnsupportedAudioFileException {
        openFFTFile(fftPath, true);
        this.fftAccuracy = fftAccuracy;
        this.samplingAccuracy = samplingAccuracy;

        putInt(fftAccuracy);//fft精度
        putInt(samplingAccuracy);//采样精度


        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioURL.toFile());
        ByteBuffer buf = ByteBuffer.wrap(audioInputStream.readAllBytes());
        buf.order(ByteOrder.LITTLE_ENDIAN);

        samplingLength = buf.capacity() / 2 / samplingAccuracy;
        putInt(samplingLength);//采样长度

        //fftData = new float[samplingLength][fftAccuracy / 2];


        ExecutorService executorService = Executors.newFixedThreadPool(36);


        for (int i = 0; i < samplingLength; i++) {


            int finalI = i;
            executorService.submit(() -> {


                Complex[] data = new Complex[fftAccuracy];


                buf.position(finalI * 2 * samplingAccuracy);

                for (int j = 0; j < fftAccuracy; j++) {
                    try {
                        data[j] = new Complex(buf.getShort(), 0);
                    } catch (Exception e) {
                        data[j] = new Complex(0, 0);
                    }
                }


                data = FFT.getFFT(data, fftAccuracy);//傅里叶变换
                Double[] x2 = Complex.toModArray(data);//计算傅里叶变换得到的复数数组的模值
                for (int k = 0; k < fftAccuracy / 2; k++) {
                    try {
                        //设置写入位置
                        position(12L + (long) finalI * fftAccuracy / 2 * 4 + k * 4L);
                        x2[fftAccuracy / 2 + k] = x2[fftAccuracy / 2 + k] / fftAccuracy * 3;

                        putFloat(x2[fftAccuracy / 2 + k].floatValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


        }


        executorService.shutdown();
        try {
            while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) ;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取某个时间点的频谱
     *
     * @param position 位置
     * @return
     *//*
    public float[] fftGet(long position) {
        //fftData[]
        //return
    }*/

    public static Complex[] getFFT(Complex[] input, int N) {
        if ((N / 2) % 2 == 0) {
            Complex[] even = new Complex[N / 2];// 偶数
            Complex[] odd = new Complex[N / 2];// 奇数
            for (int i = 0; i < N / 2; i++) {
                even[i] = input[2 * i];
                odd[i] = input[2 * i + 1];
            }
            even = getFFT(even, N / 2);
            odd = getFFT(odd, N / 2);
            for (int i = 0; i < N / 2; i++) {
                Complex[] res = Complex.butterfly(even[i], odd[i], Complex.GetW(i, N));
                input[i] = res[0];
                input[i + N / 2] = res[1];
            }
            return input;
        } else {// 两点DFT,直接进行碟形运算
            return Complex.butterfly(input[0], input[1], Complex.GetW(0, N));
        }
    }
}
