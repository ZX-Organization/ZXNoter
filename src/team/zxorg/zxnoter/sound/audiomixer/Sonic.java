/* Sonic library
   Copyright 2010, 2011 Bill Cox
   Refactored 2026 for Float-Only interface and Latency control.
   This file is part of the Sonic Library.
*/

package team.zxorg.zxnoter.sound.audiomixer;

/**
 * Sonic 音频变速变调处理库（纯浮点接口版）。
 * <p>
 * 该类使用重采样和这种叠加算法（SOLA）来实现音频的时间拉伸（变速）和音高偏移（变调）。
 * 支持流式处理。
 */
public class Sonic {

    // 默认配置
    private static final int DEFAULT_MIN_PITCH = 65;
    private static final int DEFAULT_MAX_PITCH = 400;

    // 用于加速处理的降采样频率
    private static final int SONIC_AMDF_FREQ = 4000;
    // Sinc FIR 滤波器点数
    private static final int SINC_FILTER_POINTS = 12;
    private static final int SINC_TABLE_SIZE = 601;

    // Sinc 函数查找表 (Windowed Sinc function)
    private static final short[] sincTable = {
            0, 0, 0, 0, 0, 0, 0, -1, -1, -2, -2, -3, -4, -6, -7, -9, -10, -12, -14,
            -17, -19, -21, -24, -26, -29, -32, -34, -37, -40, -42, -44, -47, -48, -50,
            -51, -52, -53, -53, -53, -52, -50, -48, -46, -43, -39, -34, -29, -22, -16,
            -8, 0, 9, 19, 29, 41, 53, 65, 79, 92, 107, 121, 137, 152, 168, 184, 200,
            215, 231, 247, 262, 276, 291, 304, 317, 328, 339, 348, 357, 363, 369, 372,
            374, 375, 373, 369, 363, 355, 345, 332, 318, 300, 281, 259, 234, 208, 178,
            147, 113, 77, 39, 0, -41, -85, -130, -177, -225, -274, -324, -375, -426,
            -478, -530, -581, -632, -682, -731, -779, -825, -870, -912, -951, -989,
            -1023, -1053, -1080, -1104, -1123, -1138, -1149, -1154, -1155, -1151,
            -1141, -1125, -1105, -1078, -1046, -1007, -963, -913, -857, -796, -728,
            -655, -576, -492, -403, -309, -210, -107, 0, 111, 225, 342, 462, 584, 708,
            833, 958, 1084, 1209, 1333, 1455, 1575, 1693, 1807, 1916, 2022, 2122, 2216,
            2304, 2384, 2457, 2522, 2579, 2625, 2663, 2689, 2706, 2711, 2705, 2687,
            2657, 2614, 2559, 2491, 2411, 2317, 2211, 2092, 1960, 1815, 1658, 1489,
            1308, 1115, 912, 698, 474, 241, 0, -249, -506, -769, -1037, -1310, -1586,
            -1864, -2144, -2424, -2703, -2980, -3254, -3523, -3787, -4043, -4291,
            -4529, -4757, -4972, -5174, -5360, -5531, -5685, -5819, -5935, -6029,
            -6101, -6150, -6175, -6175, -6149, -6096, -6015, -5905, -5767, -5599,
            -5401, -5172, -4912, -4621, -4298, -3944, -3558, -3141, -2693, -2214,
            -1705, -1166, -597, 0, 625, 1277, 1955, 2658, 3386, 4135, 4906, 5697, 6506,
            7332, 8173, 9027, 9893, 10769, 11654, 12544, 13439, 14335, 15232, 16128,
            17019, 17904, 18782, 19649, 20504, 21345, 22170, 22977, 23763, 24527,
            25268, 25982, 26669, 27327, 27953, 28547, 29107, 29632, 30119, 30569,
            30979, 31349, 31678, 31964, 32208, 32408, 32565, 32677, 32744, 32767,
            32744, 32677, 32565, 32408, 32208, 31964, 31678, 31349, 30979, 30569,
            30119, 29632, 29107, 28547, 27953, 27327, 26669, 25982, 25268, 24527,
            23763, 22977, 22170, 21345, 20504, 19649, 18782, 17904, 17019, 16128,
            15232, 14335, 13439, 12544, 11654, 10769, 9893, 9027, 8173, 7332, 6506,
            5697, 4906, 4135, 3386, 2658, 1955, 1277, 625, 0, -597, -1166, -1705,
            -2214, -2693, -3141, -3558, -3944, -4298, -4621, -4912, -5172, -5401,
            -5599, -5767, -5905, -6015, -6096, -6149, -6175, -6175, -6150, -6101,
            -6029, -5935, -5819, -5685, -5531, -5360, -5174, -4972, -4757, -4529,
            -4291, -4043, -3787, -3523, -3254, -2980, -2703, -2424, -2144, -1864,
            -1586, -1310, -1037, -769, -506, -249, 0, 241, 474, 698, 912, 1115, 1308,
            1489, 1658, 1815, 1960, 2092, 2211, 2317, 2411, 2491, 2559, 2614, 2657,
            2687, 2705, 2711, 2706, 2689, 2663, 2625, 2579, 2522, 2457, 2384, 2304,
            2216, 2122, 2022, 1916, 1807, 1693, 1575, 1455, 1333, 1209, 1084, 958, 833,
            708, 584, 462, 342, 225, 111, 0, -107, -210, -309, -403, -492, -576, -655,
            -728, -796, -857, -913, -963, -1007, -1046, -1078, -1105, -1125, -1141,
            -1151, -1155, -1154, -1149, -1138, -1123, -1104, -1080, -1053, -1023, -989,
            -951, -912, -870, -825, -779, -731, -682, -632, -581, -530, -478, -426,
            -375, -324, -274, -225, -177, -130, -85, -41, 0, 39, 77, 113, 147, 178,
            208, 234, 259, 281, 300, 318, 332, 345, 355, 363, 369, 373, 375, 374, 372,
            369, 363, 357, 348, 339, 328, 317, 304, 291, 276, 262, 247, 231, 215, 200,
            184, 168, 152, 137, 121, 107, 92, 79, 65, 53, 41, 29, 19, 9, 0, -8, -16,
            -22, -29, -34, -39, -43, -46, -48, -50, -52, -53, -53, -53, -52, -51, -50,
            -48, -47, -44, -42, -40, -37, -34, -32, -29, -26, -24, -21, -19, -17, -14,
            -12, -10, -9, -7, -6, -4, -3, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0
    };

    // 内部缓冲区（使用 short 保证原始算法性能）
    private short[] inputBuffer;
    private short[] outputBuffer;
    private short[] pitchBuffer;
    private short[] downSampleBuffer;

    private double speed;
    private float volume;
    private float pitch;
    private double rate;
    private int oldRatePosition;
    private int newRatePosition;
    private boolean useChordPitch;
    private int quality;
    private int numChannels;
    private int inputBufferSize;
    private int pitchBufferSize;
    private int outputBufferSize;
    private int numInputSamples;
    private int numOutputSamples;
    private int numPitchSamples;
    private int minPeriod;
    private int maxPeriod;
    private int maxRequired;
    private int remainingInputToCopy;
    private int sampleRate;
    private int prevPeriod;
    private int prevMinDiff;
    private int minDiff;
    private int maxDiff;

    // 音高检测范围设置
    private int minPitch;
    private int maxPitch;

    /**
     * 创建一个 Sonic 实例。
     *
     * @param sampleRate  采样率 (例如 44100)
     * @param numChannels 通道数 (例如 1 或 2)
     */
    public Sonic(int sampleRate, int numChannels) {
        this.sampleRate = sampleRate;
        this.numChannels = numChannels;
        this.minPitch = DEFAULT_MIN_PITCH;
        this.maxPitch = DEFAULT_MAX_PITCH;

        allocateStreamBuffers(sampleRate, numChannels);

        speed = 1.0f;
        pitch = 1.0f;
        volume = 1.0f;
        rate = 1.0f;
        oldRatePosition = 0;
        newRatePosition = 0;
        useChordPitch = false;
        quality = 0;
    }

    /**
     * 设置音频检测的音高范围。
     * <p>
     * 减小 maxPeriod (即增加 minPitch) 可以显著降低缓冲延迟，
     * 但如果设置过高可能会丢失低频声音的音高检测准确性。
     *
     * @param minPitch 最小音高 (Hz)，默认 65
     * @param maxPitch 最大音高 (Hz)，默认 400
     */
    public void setPitchRange(int minPitch, int maxPitch) {
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
        allocateStreamBuffers(sampleRate, numChannels);
    }

    /**
     * 获取当前流的播放速度。
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * 设置流的播放速度。
     *
     * @param speed 速度倍率 (1.0 为正常速度)
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * 获取当前流的音调。
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * 设置流的音调。
     *
     * @param pitch 音调倍率 (1.0 为正常音调)
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * 获取当前流的速率。
     */
    public double getRate() {
        return rate;
    }

    /**
     * 设置流的播放速率。这将同时改变速度和音调。
     *
     * @param rate 速率倍率
     */
    public void setRate(double rate) {
        this.rate = rate;
        this.oldRatePosition = 0;
        this.newRatePosition = 0;
    }

    /**
     * 获取是否使用和弦音高计算模式。
     */
    public boolean getChordPitch() {
        return useChordPitch;
    }

    /**
     * 设置是否使用和弦模式进行音高计算。默认关闭。
     *
     * @param useChordPitch true 开启
     */
    public void setChordPitch(boolean useChordPitch) {
        this.useChordPitch = useChordPitch;
    }

    /**
     * 获取质量设置。
     */
    public int getQuality() {
        return quality;
    }

    /**
     * 设置处理质量。
     *
     * @param quality 0 为默认质量（最快），1 为高质量（较慢）
     */
    public void setQuality(int quality) {
        this.quality = quality;
    }

    /**
     * 获取音量缩放因子。
     */
    public float getVolume() {
        return volume;
    }

    /**
     * 设置音量缩放因子。
     *
     * @param volume 音量倍率 (1.0 为原始音量)
     */
    public void setVolume(float volume) {
        this.volume = volume;
    }

    /**
     * 获取采样率。
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * 设置采样率。这将导致缓冲区中的样本丢失。
     */
    public void setSampleRate(int sampleRate) {
        allocateStreamBuffers(sampleRate, numChannels);
    }

    /**
     * 获取通道数。
     */
    public int getNumChannels() {
        return numChannels;
    }

    /**
     * 设置通道数。这将导致缓冲区中的样本丢失。
     */
    public void setNumChannels(int numChannels) {
        allocateStreamBuffers(sampleRate, numChannels);
    }

    /**
     * 立即清空所有内部缓冲区和状态。
     * 用于切歌或停止播放时，避免残留音频。
     */
    public void clear() {
        numInputSamples = 0;
        numOutputSamples = 0;
        numPitchSamples = 0;
        remainingInputToCopy = 0;
        oldRatePosition = 0;
        newRatePosition = 0;
        prevPeriod = 0;
        prevMinDiff = 0;
    }

    /**
     * 强制处理并刷新流中的剩余数据。
     * 在流结束时调用，以确保所有输入样本都被处理和输出。
     * 注意：这可能会在末尾引入少量静音。
     */
    public void flush() {
        int remainingSamples = numInputSamples;
        double s = speed / pitch;
        double r = rate * pitch;
        int expectedOutputSamples = numOutputSamples + (int) ((remainingSamples / s + numPitchSamples) / r + 0.5f);

        // Add enough silence to flush both input and pitch buffers.
        enlargeInputBufferIfNeeded(remainingSamples + 2 * maxRequired);
        for (int xSample = 0; xSample < 2 * maxRequired * numChannels; xSample++) {
            inputBuffer[remainingSamples * numChannels + xSample] = 0;
        }
        numInputSamples += 2 * maxRequired;
        write(null, 0); // Trigger processing

        // Throw away any extra samples we generated due to the silence we added.
        if (numOutputSamples > expectedOutputSamples) {
            numOutputSamples = expectedOutputSamples;
        }

        // Empty input and pitch buffers.
        numInputSamples = 0;
        remainingInputToCopy = 0;
        numPitchSamples = 0;
    }

    /**
     * 返回输出缓冲区中可读取的样本数量。
     */
    public int samplesAvailable() {
        return numOutputSamples;
    }

    /**
     * 写入浮点音频数据到流中。
     *
     * @param samples    输入样本数组
     * @param numSamples 样本数量（每通道）
     */
    public void write(float[] samples, int numSamples) {
        if (numSamples == 0 || samples == null) {
            // 允许传入 null/0 来触发内部处理逻辑（flush 时使用）
            processStreamInput();
            return;
        }
        enlargeInputBufferIfNeeded(numSamples);
        int xBuffer = numInputSamples * numChannels;
        for (int xSample = 0; xSample < numSamples * numChannels; xSample++) {
            // 转换为 short 并限幅
            inputBuffer[xBuffer++] = (short) (samples[xSample] * 32767.0f);
        }
        numInputSamples += numSamples;
        processStreamInput();
    }

    /**
     * 从流中读取浮点音频数据。
     *
     * @param samples    用于存储输出的数组
     * @param maxSamples 最大读取样本数（每通道）
     * @return 实际读取的样本数
     */
    public int read(float[] samples, int maxSamples) {
        int numSamples = numOutputSamples;
        int remainingSamples = 0;

        if (numSamples == 0) {
            return 0;
        }
        if (numSamples > maxSamples) {
            remainingSamples = numSamples - maxSamples;
            numSamples = maxSamples;
        }
        for (int xSample = 0; xSample < numSamples * numChannels; xSample++) {
            samples[xSample] = (outputBuffer[xSample]) / 32767.0f;
        }
        move(outputBuffer, 0, outputBuffer, numSamples, remainingSamples);
        numOutputSamples = remainingSamples;
        return numSamples;
    }

    // ================= 私有内部方法 =================

    private void allocateStreamBuffers(int sampleRate, int numChannels) {
        this.sampleRate = sampleRate;
        this.numChannels = numChannels;

        // 基于 min/max pitch 计算缓冲区大小，这直接影响延迟
        minPeriod = sampleRate / maxPitch;
        maxPeriod = sampleRate / minPitch;

        maxRequired = 2 * maxPeriod;
        inputBufferSize = maxRequired;
        inputBuffer = new short[maxRequired * numChannels];
        outputBufferSize = maxRequired;
        outputBuffer = new short[maxRequired * numChannels];
        pitchBufferSize = maxRequired;
        pitchBuffer = new short[maxRequired * numChannels];
        downSampleBuffer = new short[maxRequired];

        clear(); // 分配缓冲区时重置状态
    }

    private short[] resize(short[] oldArray, int newLength) {
        newLength *= numChannels;
        short[] newArray = new short[newLength];
        int length = Math.min(oldArray.length, newLength);
        System.arraycopy(oldArray, 0, newArray, 0, length);
        return newArray;
    }

    private void move(short[] dest, int destPos, short[] source, int sourcePos, int numSamples) {
        System.arraycopy(source, sourcePos * numChannels, dest, destPos * numChannels, numSamples * numChannels);
    }

    private void scaleSamples(short[] samples, int position, int numSamples, float volume) {
        int fixedPointVolume = (int) (volume * 4096.0f);
        int start = position * numChannels;
        int stop = start + numSamples * numChannels;

        for (int xSample = start; xSample < stop; xSample++) {
            int value = (samples[xSample] * fixedPointVolume) >> 12;
            if (value > 32767) {
                value = 32767;
            } else if (value < -32767) {
                value = -32767;
            }
            samples[xSample] = (short) value;
        }
    }

    private void enlargeOutputBufferIfNeeded(int numSamples) {
        if (numOutputSamples + numSamples > outputBufferSize) {
            outputBufferSize += (outputBufferSize >> 1) + numSamples;
            outputBuffer = resize(outputBuffer, outputBufferSize);
        }
    }

    private void enlargeInputBufferIfNeeded(int numSamples) {
        if (numInputSamples + numSamples > inputBufferSize) {
            inputBufferSize += (inputBufferSize >> 1) + numSamples;
            inputBuffer = resize(inputBuffer, inputBufferSize);
        }
    }

    private void removeInputSamples(int position) {
        int remainingSamples = numInputSamples - position;
        move(inputBuffer, 0, inputBuffer, position, remainingSamples);
        numInputSamples = remainingSamples;
    }

    private void copyToOutput(short[] samples, int position, int numSamples) {
        enlargeOutputBufferIfNeeded(numSamples);
        move(outputBuffer, numOutputSamples, samples, position, numSamples);
        numOutputSamples += numSamples;
    }

    private int copyInputToOutput(int position) {
        int numSamples = remainingInputToCopy;
        if (numSamples > maxRequired) {
            numSamples = maxRequired;
        }
        copyToOutput(inputBuffer, position, numSamples);
        remainingInputToCopy -= numSamples;
        return numSamples;
    }

    private void downSampleInput(short[] samples, int position, int skip) {
        int numSamples = maxRequired / skip;
        int samplesPerValue = numChannels * skip;
        int value;

        position *= numChannels;
        for (int i = 0; i < numSamples; i++) {
            value = 0;
            for (int j = 0; j < samplesPerValue; j++) {
                value += samples[position + i * samplesPerValue + j];
            }
            value /= samplesPerValue;
            downSampleBuffer[i] = (short) value;
        }
    }

    private int findPitchPeriodInRange(short[] samples, int position, int minPeriod, int maxPeriod) {
        int bestPeriod = 0, worstPeriod = 255;
        int minDiff = 1, maxDiff = 0;

        position *= numChannels;
        for (int period = minPeriod; period <= maxPeriod; period++) {
            int diff = 0;
            for (int i = 0; i < period; i++) {
                short sVal = samples[position + i];
                short pVal = samples[position + period + i];
                diff += sVal >= pVal ? sVal - pVal : pVal - sVal;
            }
            if (diff * bestPeriod < minDiff * period) {
                minDiff = diff;
                bestPeriod = period;
            }
            if (diff * worstPeriod > maxDiff * period) {
                maxDiff = diff;
                worstPeriod = period;
            }
        }
        this.minDiff = minDiff / bestPeriod;
        this.maxDiff = maxDiff / worstPeriod;

        return bestPeriod;
    }

    private boolean prevPeriodBetter(int minDiff, int maxDiff, boolean preferNewPeriod) {
        if (minDiff == 0 || prevPeriod == 0) {
            return false;
        }
        if (preferNewPeriod) {
            if (maxDiff > minDiff * 3) {
                return false;
            }
            if (minDiff * 2 <= prevMinDiff * 3) {
                return false;
            }
        } else {
            if (minDiff <= prevMinDiff) {
                return false;
            }
        }
        return true;
    }

    private int findPitchPeriod(short[] samples, int position, boolean preferNewPeriod) {
        int period, retPeriod;
        int skip = 1;

        if (sampleRate > SONIC_AMDF_FREQ && quality == 0) {
            skip = sampleRate / SONIC_AMDF_FREQ;
        }
        if (numChannels == 1 && skip == 1) {
            period = findPitchPeriodInRange(samples, position, minPeriod, maxPeriod);
        } else {
            downSampleInput(samples, position, skip);
            period = findPitchPeriodInRange(downSampleBuffer, 0, minPeriod / skip, maxPeriod / skip);
            if (skip != 1) {
                period *= skip;
                int minP = period - (skip << 2);
                int maxP = period + (skip << 2);
                if (minP < minPeriod) {
                    minP = minPeriod;
                }
                if (maxP > maxPeriod) {
                    maxP = maxPeriod;
                }
                if (numChannels == 1) {
                    period = findPitchPeriodInRange(samples, position, minP, maxP);
                } else {
                    downSampleInput(samples, position, 1);
                    period = findPitchPeriodInRange(downSampleBuffer, 0, minP, maxP);
                }
            }
        }
        if (prevPeriodBetter(minDiff, maxDiff, preferNewPeriod)) {
            retPeriod = prevPeriod;
        } else {
            retPeriod = period;
        }
        prevMinDiff = minDiff;
        prevPeriod = period;
        return retPeriod;
    }

    private void overlapAdd(int numSamples, int numChannels, short[] out, int outPos, short[] rampDown, int rampDownPos, short[] rampUp, int rampUpPos) {
        for (int i = 0; i < numChannels; i++) {
            int o = outPos * numChannels + i;
            int u = rampUpPos * numChannels + i;
            int d = rampDownPos * numChannels + i;
            for (int t = 0; t < numSamples; t++) {
                out[o] = (short) ((rampDown[d] * (numSamples - t) + rampUp[u] * t) / numSamples);
                o += numChannels;
                d += numChannels;
                u += numChannels;
            }
        }
    }

    private void overlapAddWithSeparation(int numSamples, int numChannels, int separation, short[] out, int outPos, short[] rampDown, int rampDownPos, short[] rampUp, int rampUpPos) {
        for (int i = 0; i < numChannels; i++) {
            int o = outPos * numChannels + i;
            int u = rampUpPos * numChannels + i;
            int d = rampDownPos * numChannels + i;
            for (int t = 0; t < numSamples + separation; t++) {
                if (t < separation) {
                    out[o] = (short) (rampDown[d] * (numSamples - t) / numSamples);
                    d += numChannels;
                } else if (t < numSamples) {
                    out[o] = (short) ((rampDown[d] * (numSamples - t) + rampUp[u] * (t - separation)) / numSamples);
                    d += numChannels;
                    u += numChannels;
                } else {
                    out[o] = (short) (rampUp[u] * (t - separation) / numSamples);
                    u += numChannels;
                }
                o += numChannels;
            }
        }
    }

    private void moveNewSamplesToPitchBuffer(int originalNumOutputSamples) {
        int numSamples = numOutputSamples - originalNumOutputSamples;

        if (numPitchSamples + numSamples > pitchBufferSize) {
            pitchBufferSize += (pitchBufferSize >> 1) + numSamples;
            pitchBuffer = resize(pitchBuffer, pitchBufferSize);
        }
        move(pitchBuffer, numPitchSamples, outputBuffer, originalNumOutputSamples, numSamples);
        numOutputSamples = originalNumOutputSamples;
        numPitchSamples += numSamples;
    }

    private void removePitchSamples(int numSamples) {
        if (numSamples == 0) {
            return;
        }
        move(pitchBuffer, 0, pitchBuffer, numSamples, numPitchSamples - numSamples);
        numPitchSamples -= numSamples;
    }

    private void adjustPitch(int originalNumOutputSamples) {
        int period, newPeriod, separation;
        int position = 0;

        if (numOutputSamples == originalNumOutputSamples) {
            return;
        }
        moveNewSamplesToPitchBuffer(originalNumOutputSamples);
        while (numPitchSamples - position >= maxRequired) {
            period = findPitchPeriod(pitchBuffer, position, false);
            newPeriod = (int) (period / pitch);
            enlargeOutputBufferIfNeeded(newPeriod);
            if (pitch >= 1.0f) {
                overlapAdd(newPeriod, numChannels, outputBuffer, numOutputSamples, pitchBuffer,
                        position, pitchBuffer, position + period - newPeriod);
            } else {
                separation = newPeriod - period;
                overlapAddWithSeparation(period, numChannels, separation, outputBuffer, numOutputSamples,
                        pitchBuffer, position, pitchBuffer, position);
            }
            numOutputSamples += newPeriod;
            position += period;
        }
        removePitchSamples(position);
    }

    private int findSincCoefficient(int i, int ratio, int width) {
        int lobePoints = (SINC_TABLE_SIZE - 1) / SINC_FILTER_POINTS;
        int left = i * lobePoints + (ratio * lobePoints) / width;
        int right = left + 1;
        int position = i * lobePoints * width + ratio * lobePoints - left * width;
        if (left < 0) left = 0;
        if (left >= sincTable.length) left = sincTable.length - 1;
        if (right < 0) right = 0;
        if (right >= sincTable.length) right = sincTable.length - 1;
        int leftVal = sincTable[left];
        int rightVal = sincTable[right];

        return ((leftVal * (width - position) + rightVal * position) << 1) / width;
    }

    private int getSign(int value) {
        return value >= 0 ? 1 : -1;
    }

    private short interpolate(short[] in, int inPos, int oldSampleRate, int newSampleRate) {
        int i;
        int total = 0;
        int position = newRatePosition * oldSampleRate;
        int leftPosition = oldRatePosition * newSampleRate;
        int rightPosition = (oldRatePosition + 1) * newSampleRate;
        int ratio = rightPosition - position - 1;
        int width = rightPosition - leftPosition;
        int weight, value;
        int oldSign;
        int overflowCount = 0;

        for (i = 0; i < SINC_FILTER_POINTS; i++) {
            weight = findSincCoefficient(i, ratio, width);
            value = in[inPos + i * numChannels] * weight;
            oldSign = getSign(total);
            total += value;
            if (oldSign != getSign(total) && getSign(value) == oldSign) {
                overflowCount += oldSign;
            }
        }
        if (overflowCount > 0) {
            return Short.MAX_VALUE;
        } else if (overflowCount < 0) {
            return Short.MIN_VALUE;
        }
        return (short) (total >> 16);
    }

    private void adjustRate(double rate, int originalNumOutputSamples) {
        int newSampleRate = (int) (sampleRate / rate);
        int oldSampleRate = sampleRate;
        int position;
        int N = SINC_FILTER_POINTS;

        while (newSampleRate > (1 << 14) || oldSampleRate > (1 << 14)) {
            newSampleRate >>= 1;
            oldSampleRate >>= 1;
        }
        if (numOutputSamples == originalNumOutputSamples) {
            return;
        }
        moveNewSamplesToPitchBuffer(originalNumOutputSamples);
        for (position = 0; position < numPitchSamples - N; position++) {
            while ((oldRatePosition + 1) * newSampleRate > newRatePosition * oldSampleRate) {
                enlargeOutputBufferIfNeeded(1);
                for (int i = 0; i < numChannels; i++) {
                    outputBuffer[numOutputSamples * numChannels + i] = interpolate(pitchBuffer,
                            position * numChannels + i, oldSampleRate, newSampleRate);
                }
                newRatePosition++;
                numOutputSamples++;
            }
            oldRatePosition++;
            if (oldRatePosition == oldSampleRate) {
                oldRatePosition = 0;
                newRatePosition = 0;
            }
        }
        removePitchSamples(position);
    }

    private int skipPitchPeriod(short[] samples, int position, double speed, int period) {
        int newSamples;
        if (speed >= 2.0f) {
            newSamples = (int) (period / (speed - 1.0f));
        } else {
            newSamples = period;
            remainingInputToCopy = (int) (period * (2.0f - speed) / (speed - 1.0f));
        }
        enlargeOutputBufferIfNeeded(newSamples);
        overlapAdd(newSamples, numChannels, outputBuffer, numOutputSamples, samples, position,
                samples, position + period);
        numOutputSamples += newSamples;
        return newSamples;
    }

    private int insertPitchPeriod(short[] samples, int position, double speed, int period) {
        int newSamples;
        if (speed < 0.5f) {
            newSamples = (int) (period * speed / (1.0f - speed));
        } else {
            newSamples = period;
            remainingInputToCopy = (int) (period * (2.0f * speed - 1.0f) / (1.0f - speed));
        }
        enlargeOutputBufferIfNeeded(period + newSamples);
        move(outputBuffer, numOutputSamples, samples, position, period);
        overlapAdd(newSamples, numChannels, outputBuffer, numOutputSamples + period, samples,
                position + period, samples, position);
        numOutputSamples += period + newSamples;
        return newSamples;
    }

    private void changeSpeed(double speed) {
        int numSamples = numInputSamples;
        int position = 0, period, newSamples;

        if (numInputSamples < maxRequired) {
            return;
        }
        do {
            if (remainingInputToCopy > 0) {
                newSamples = copyInputToOutput(position);
                position += newSamples;
            } else {
                period = findPitchPeriod(inputBuffer, position, true);
                if (speed > 1.0) {
                    newSamples = skipPitchPeriod(inputBuffer, position, speed, period);
                    position += period + newSamples;
                } else {
                    newSamples = insertPitchPeriod(inputBuffer, position, speed, period);
                    position += newSamples;
                }
            }
        } while (position + maxRequired <= numSamples);
        removeInputSamples(position);
    }

    private void processStreamInput() {
        int originalNumOutputSamples = numOutputSamples;
        double s = speed / pitch;
        double r = rate;

        if (!useChordPitch) {
            r *= pitch;
        }
        if (s > 1.00001 || s < 0.99999) {
            changeSpeed(s);
        } else {
            copyToOutput(inputBuffer, 0, numInputSamples);
            numInputSamples = 0;
        }
        if (useChordPitch) {
            if (pitch != 1.0f) {
                adjustPitch(originalNumOutputSamples);
            }
        } else if (r != 1.0f) {
            adjustRate(r, originalNumOutputSamples);
        }
        if (volume != 1.0f) {
            scaleSamples(outputBuffer, originalNumOutputSamples, numOutputSamples - originalNumOutputSamples, volume);
        }
    }
}