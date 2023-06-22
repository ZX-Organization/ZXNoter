package audiochannel.audiomixer_t.channel;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;

public enum AudioBitDepth {
    BYTE_8_BIT(1, Byte.class),
    SHORT_16_BIT(2, Short.class),
    INTEGER_32_BIT(4, Integer.class),
    LONG_64_BIT(8, Long.class);


    private final int byteLength;
    private final Class<?> dataType;


    AudioBitDepth(int byteLength, Class<?> dataType) {
        this.byteLength = byteLength;
        this.dataType = dataType;
    }

    /**
     * 获取位深字节大小
     *
     * @return 字节数
     */
    public int getByteLength() {
        return byteLength;
    }
    /**
     * 获取位深大小
     *
     * @return 位深
     */
    public int getBitLength() {
        return byteLength*8;
    }
    /**
     * 获取数据类型
     *
     * @return 数据类型Class
     */
    public Class<?> getDataType() {
        return dataType;
    }

    /**
     * 通过位深获取枚举
     *
     * @param bitDepth 位深
     * @return 对应的位深枚举类
     */
    public static AudioBitDepth get(int bitDepth) {
        return switch (bitDepth) {
            case 8 -> BYTE_8_BIT;
            case 16 -> SHORT_16_BIT;
            case 32 -> INTEGER_32_BIT;
            case 64 -> LONG_64_BIT;
            default -> throw new IllegalStateException("Bit Depth Error Unexpected value: " + bitDepth);
        };
    }

    /**
     * 通过音频格式获取枚举
     *
     * @param audioFormat 音频格式
     * @return 对应的位深枚举类
     */
    public static AudioBitDepth get(AudioFormat audioFormat) {
        return get(audioFormat.getSampleSizeInBits());
    }

    /**
     * 获取采样
     *
     * @param buf 缓冲区
     * @return 原始采样值
     */
    public Number getSample(ByteBuffer buf) {
        return switch (this) {
            case BYTE_8_BIT -> buf.get();
            case SHORT_16_BIT -> buf.getShort();
            case INTEGER_32_BIT -> buf.getInt();
            case LONG_64_BIT -> buf.getLong();
        };
    }

    /**
     * 获取采样
     *
     * @param buf             缓冲区
     * @param toAudioBitDepth 转换的位深
     * @return 转换后的采样值
     */
    public Number getSample(ByteBuffer buf, AudioBitDepth toAudioBitDepth) {
        if (this == toAudioBitDepth) {
            return getSample(buf);
        }
        int bitDifference = (toAudioBitDepth.getByteLength() - this.getByteLength()) * 8;
        long sample = getSample(buf).longValue();
        return (bitDifference > 0 ? sample << bitDifference : sample >> -bitDifference);
    }


}
