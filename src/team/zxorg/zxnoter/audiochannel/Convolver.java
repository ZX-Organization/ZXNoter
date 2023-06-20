package team.zxorg.zxnoter.audiochannel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 卷积器类，用于音频信号的卷积处理。
 */
public class Convolver {
    private Complex[] impulseResponseFFT;
    float[] impulseResponse;

    /**
     * 创建卷积器对象，并加载脉冲响应音频流进行初始化。
     *
     * @param audioFormat           音频格式
     * @param impulseResponseStream 脉冲响应音频流
     * @throws IOException 如果读取脉冲响应音频流时发生错误
     */
    public Convolver(AudioFormat audioFormat, AudioInputStream impulseResponseStream) throws IOException {
        int impulseResponseLength = (int) (impulseResponseStream.getFrameLength() * audioFormat.getFrameSize());
        ByteBuffer impulseResponseBuffer = ByteBuffer.allocate(impulseResponseLength);
        impulseResponseStream.read(impulseResponseBuffer.array());
        impulseResponseBuffer.order((audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN));

        // 将字节数据转换为复数数组
        impulseResponseBuffer.rewind();
        impulseResponseFFT = new Complex[impulseResponseLength / 2];
        impulseResponse = new float[impulseResponseLength / 2];
        for (int i = 0; i < impulseResponseFFT.length; i++) {
            short sample = impulseResponseBuffer.getShort();
            impulseResponseFFT[i] = new Complex((double) sample / 32767.0, 0);
            impulseResponse[i] = (float) sample / 32767.0f;
        }

        // 对脉冲响应进行FFT变换
        FFT.fft(impulseResponseFFT);
    }

    /**
     * 对输入样本进行卷积处理。
     *
     * @param samples 输入样本数组
     * @return 处理后的样本数组
     */
    public int[] process(int[] samples) {
        int[] processedSamples = new int[samples.length];
        for (int i = 0; i < samples.length; i++) {
            float convolvedValue = 0.0f;
            for (int j = 0; j < impulseResponse.length / 30; j++) {
                if (i - j >= 0) {
                    convolvedValue += impulseResponse[j] * samples[i - j];
                }
            }
            processedSamples[i] = (int) (convolvedValue * 0.4f);
        }
        return processedSamples;
    }

    /**
     * 对输入样本进行卷积处理，使用FFT算法进行加速。
     *
     * @param samples 输入样本数组
     * @return 处理后的样本数组
     */
    public int[] processFFT(int[] samples) {
        int[] processedSamples = new int[samples.length];
        Complex[] samplesFFT = new Complex[samples.length];

        // 将样本数据转换为复数数组
        for (int i = 0; i < samples.length; i++) {
            samplesFFT[i] = new Complex(samples[i], 0);
        }

        // 对样本数据进行FFT变换
        FFT.fft(samplesFFT);

        // 对频域数据进行乘法运算
        for (int i = 0; i < samplesFFT.length; i++) {
            samplesFFT[i] = samplesFFT[i].times(impulseResponseFFT[i]).times(new Complex(0.002, 0.002));
        }

        // 对结果进行反向FFT变换
        FFT.ifft(samplesFFT);

        // 提取实部作为处理后的样本
        for (int i = 0; i < processedSamples.length; i++) {
            processedSamples[i] = (int) samplesFFT[i].re();
        }

        return processedSamples;
    }
}