package team.zxorg.zxncore.test.fft;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class FFT {
    protected RandomAccessFile randomAccessFile;//打开的FFT文件

    public int getFFTAccuracy() {
        return fftAccuracy;
    }

    public int getSampleAccuracy() {
        return sampleAccuracy;
    }

    public float getAudioSampleRate() {
        return audioSampleRate;
    }

    public int getSampleLength() {
        return sampleLength;
    }

    protected FileChannel fileChannel;//FFT文件通道
    protected MappedByteBuffer byteBuffer;//FFT文件字节缓冲区


    protected long byteBufferPosition;//字节缓冲区位置
    protected final int byteBufferSize = 1024 * 1024 * 60;//字节缓冲区大小

    protected final int fftBufferSize = 512;//FFT缓冲区大小
    protected long fftBufferPosition;//FFT缓冲区绝对位置
    protected float[][] fftData;//FFT缓冲区


    protected int fftAccuracy;//fft精度
    protected int sampleAccuracy;//采样精度
    protected int sampleLength;//采样长度
    protected float audioSampleRate;//音频采样率

    protected void putFloat(float f) {
        checkBuf(4);
        byteBuffer.putFloat(f);
    }

    protected float getFloat() {
        checkBuf(4);
        return byteBuffer.getFloat();
    }

    protected void putInt(int i) {
        checkBuf(4);
        //System.out.println("put");
        byteBuffer.putInt(i);
    }

    protected int getInt() {
        checkBuf(4);
        return byteBuffer.getInt();
    }

    protected void putLong(long l) {
        checkBuf(8);
        byteBuffer.putLong(l);
    }

    protected long getLong() {
        checkBuf(4);
        return byteBuffer.getLong();
    }

    /**
     * 获取当前的位置
     *
     * @return
     */
    protected long bufferPosition() {
        return byteBufferPosition + byteBuffer.position();
    }

    /**
     * 设置读取的位置
     *
     * @param newPosition 新的位置
     */
    protected void bufferPosition(long newPosition) {
        //现位置起始和新位置的 距离
        long gap = newPosition - byteBufferPosition;
        if (gap > byteBuffer.capacity() || gap < 0) {//在右边 在左边
            //重新分配
            byteBuffer.clear();
            byteBufferPosition = newPosition;
            try {
                byteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, newPosition, byteBufferSize);
            } catch (IOException e) {
                //throw new RuntimeException(e);
            }
        } else {//在里面
            byteBuffer.position((int) (newPosition - byteBufferPosition));
        }

    }

    /**
     * 检查读取是否在缓冲区内
     *
     * @param size 需要读取的字节数
     */
    protected void checkBuf(int size) {
        int outByteSize = (byteBuffer.position() + size) - byteBufferSize;
        if (outByteSize > 0) {
            //System.out.println("超出缓冲区，进行重映射");
            byteBuffer.force();
            byteBuffer.clear();
            byteBufferPosition = byteBufferPosition + byteBufferSize - (size - outByteSize);
            try {
                byteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, byteBufferPosition, byteBufferSize);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 打开文件
     *
     * @param fftPath 文件目录
     * @param rewrite 重写
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
        this.sampleAccuracy = getInt();//采样精度
        this.audioSampleRate = getFloat();//采样率
        this.sampleLength = getInt();//采样长度
    }

    /**
     * 通过音频创建fft对象
     *
     * @param fftPath        要存储的fft文件路径
     * @param audioPath      音频路径
     * @param fftAccuracy    fft精度
     * @param sampleAccuracy 采样精度
     */
    public FFT(Path fftPath, Path audioPath, int fftAccuracy, int sampleAccuracy) throws IOException, UnsupportedAudioFileException {
        openFFTFile(fftPath, true);
        this.fftAccuracy = fftAccuracy;
        this.sampleAccuracy = sampleAccuracy;


        putInt(fftAccuracy);//fft精度
        putInt(sampleAccuracy);//采样精度


        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioPath.toFile());
        audioSampleRate = audioInputStream.getFormat().getSampleRate();
        putFloat(audioSampleRate);//采样率
        ByteBuffer auBuf = ByteBuffer.wrap(audioInputStream.readAllBytes());
        auBuf.order(ByteOrder.LITTLE_ENDIAN);

        sampleLength = auBuf.capacity() / 2 / sampleAccuracy;
        putInt(sampleLength);//采样长度

        //fftData = new float[samplingLength][fftAccuracy / 2];


        ExecutorService executorService = Executors.newFixedThreadPool(6);
        AtomicReference<Long> lock = new AtomicReference<>(0L);

        for (int i = 0; i < sampleLength; i++) {


            int finalI = i;//当前任务的索引
            executorService.submit(() -> {

                Complex[] data = new Complex[fftAccuracy];

                synchronized (lock) {//读取锁

                    auBuf.position(finalI * 2 * sampleAccuracy);
                    for (int j = 0; j < fftAccuracy; j++) {
                        float v = 0;
                        for (int k = 0; k < sampleAccuracy; k++) {
                            v +=auBuf.getShort();
                        }
                        v/=sampleAccuracy;

                        try {
                            data[j] = new Complex(v , 0);
                        } catch (Exception e) {
                            data[j] = new Complex(0, 0);
                        }
                    }
                }


                data = FFT.getFFT(data, fftAccuracy);//傅里叶变换
                Double[] x2 = Complex.toModArray(data);//计算傅里叶变换得到的复数数组的模值


                synchronized (lock) {//写入锁
                    while (lock.get() != finalI) {//如果不是自己则继续等待
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    bufferPosition(16L + (long) finalI * (fftAccuracy / 2) * 4);
                    for (int k = fftAccuracy / 2; k < fftAccuracy; k++) {//取一半
                        //设置写入位置
                        putFloat((float) (x2[k] / fftAccuracy));
                    }
                    lock.set(lock.get() + 1);
                    lock.notifyAll();
                }

            });

        }


        executorService.shutdown();//准备关闭线程池
        try {
            while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) ;//等待线程池结束
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        audioInputStream.close();//释放音频
    }

    /**
     * 时间到采样位置
     *
     * @param ms 时间点 ms
     * @return 返回fft绝对位置
     */
    public long fftTimeToPosition(long ms) {
        return (long) (ms * audioSampleRate / sampleAccuracy / 1000);
    }


    /**
     * 获取某个时间点的频谱
     *
     * @param position fft位置
     * @return
     */
    public float[] fftGet(long position) {
        if (fftData == null) {
            fftData = new float[1800 * 100*10][fftAccuracy / 2];//初始化fft缓冲区
            fftBufferPosition = 1;
        }

        //检查是否在缓冲区内
        {
            if (position - fftBufferPosition >= fftData.length || position - fftBufferPosition < 0) {//超出缓冲区
                //System.out.println("超出缓冲区");
                fftBufferPosition = position;//重新缓存数据
                bufferPosition(16L + position * fftAccuracy / 2 * 4);
                //System.out.println(16L + position * fftAccuracy / 2 * 4);
                for (int i = 0; i < fftData.length; i++) {//每个fft
                    for (int j = 0; j < fftAccuracy / 2; j++) {//每个值
                        fftData[i][j] = getFloat();
                    }
                }
            }
        }

        return fftData[(int) (position - fftBufferPosition)];
    }


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
