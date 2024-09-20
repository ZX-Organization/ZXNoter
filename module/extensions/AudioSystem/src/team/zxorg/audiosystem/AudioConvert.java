package team.zxorg.audiosystem;

import javax.sound.sampled.AudioFormat;

public class AudioConvert {

    public static AudioFormat getAudioFormat(int sampleRate, int channels) {
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, channels, channels * 2, sampleRate, false);
    }

    /**
     * 将 float[] 转换为 short 的 byte[]。
     *
     * @param input  输入的 float[] 数据
     * @param output 转换后的 byte[] 数据
     */
    public static void convertFloatToShortByte(float[] input, byte[] output) {
        if (input.length * 2 != output.length) {
            throw new IllegalArgumentException("output byte array length must be twice the length of the input float array.");
        }

        for (int i = 0; i < input.length; i++) {
            // 将 float 值限制在 -1.0 到 1.0 之间
            float sample = Math.min(Math.max(input[i], -1.0f), 1.0f);

            // 将 float 值转换为 short
            short shortSample = (short) (sample * 32767);

            // 将 short 值转换为两个字节，并存储在 lineBuffer 中
            output[i * 2] = (byte) (shortSample & 0xFF); // 低位字节
            output[i * 2 + 1] = (byte) ((shortSample >> 8) & 0xFF); // 高位字节
        }
    }

    /**
     * 将 short 的 byte[] 转换为 float[]。
     *
     * @param input  输入的 byte[] 数据（每两个字节表示一个 short）
     * @param output 转换后的 float[] 数据
     */
    public static void convertShortByteToFloat(byte[] input, float[] output) {
        if (input.length != output.length * 2) {
            throw new IllegalArgumentException("Input byte array length must be twice the length of the output float array.");
        }

        for (int i = 0; i < output.length; i++) {
            // 将两个字节转换为 short
            int low = input[i * 2] & 0xFF;
            int high = input[i * 2 + 1] & 0xFF;
            short shortSample = (short) ((high << 8) | low);

            // 将 short 值转换为 float
            output[i] = shortSample / 32768.0f;
        }
    }


}
