package team.zxorg.zxnoter.sound.audiomixer;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Logger;

public class AudioChannel {
    private static final Logger logger = Logger.getLogger(AudioChannel.class.getName());

    public enum PlayState { PLAY, PAUSE, STOPPED, CLOSE, END }
    public enum EndBehavior { PAUSE, LOOP, CLOSE }

    private final AudioData data;
    private final AtomicReference<PlayState> state = new AtomicReference<>(PlayState.PAUSE);
    private volatile EndBehavior endBehavior = EndBehavior.CLOSE;

    private volatile float volume = 1.0f;
    private volatile float pan = 0.0f;
    private volatile boolean isMuted = false;

    private final AtomicInteger speedChangeVersion = new AtomicInteger(0);
    private volatile double targetSpeed = 1.0;
    private volatile double targetPitch = 1.0;

    private volatile double preciseTimeMs = 0.0;
    private volatile long lastUpdateTimeNano = 0;
    private double consumerRenderSpeed = 1.0;
    private volatile double interpolationSpeed = 1.0;

    // syncTime 用于强制纠正时间轴
    private record SpeedMarker(long absoluteIndex, double speed, double syncTime) {}
    private final ConcurrentLinkedQueue<SpeedMarker> speedCheckpoints = new ConcurrentLinkedQueue<>();

    private volatile boolean seekRequest = false;
    private volatile long seekTargetSample = 0;

    private final RingBuffer outputRingBuffer;
    private final DSPWorker worker;

    public AudioChannel(AudioData data) {
        this.data = data;
        this.outputRingBuffer = new RingBuffer(2048 * data.channels);
        this.worker = new DSPWorker();
        this.worker.start();
    }

    public void pullAudio(float[] outL, float[] outR, int framesNeeded, int hardwarePendingFrames) {
        int samplesNeeded = framesNeeded * data.channels;
        float[] tempBuf = new float[samplesNeeded];

        long startReadPos = outputRingBuffer.getTotalRead();
        int samplesRead = outputRingBuffer.read(tempBuf, 0, samplesNeeded);

        if (samplesRead < samplesNeeded) {
            java.util.Arrays.fill(tempBuf, samplesRead, samplesNeeded, 0.0f);
        }

        float vol = isMuted ? 0.0f : volume;
        float leftGain = vol * (pan > 0 ? 1.0f - pan : 1.0f);
        float rightGain = vol * (pan < 0 ? 1.0f + pan : 1.0f);

        for (int i = 0; i < framesNeeded; i++) {
            outL[i] = tempBuf[i * 2] * leftGain;
            outR[i] = tempBuf[i * 2 + 1] * rightGain;
        }

        if (state.get() == PlayState.PLAY) {
            long currentReadPos = startReadPos;
            int framesProcessed = 0;

            while (!speedCheckpoints.isEmpty()) {
                SpeedMarker marker = speedCheckpoints.peek();
                long markerFrameIdx = marker.absoluteIndex / data.channels;
                long currentFrameIdx = currentReadPos / data.channels;

                if (currentFrameIdx <= (startReadPos + samplesRead) / data.channels) {
                    long framesBeforeMarker = Math.max(0, markerFrameIdx - currentFrameIdx);

                    // 累加 Marker 之前的时间
                    preciseTimeMs += (framesBeforeMarker * consumerRenderSpeed / data.sampleRate) * 1000.0;

                    framesProcessed += framesBeforeMarker;
                    currentReadPos += framesBeforeMarker * data.channels;

                    // 应用新速度并强制同步时间，解决回滚错位问题
                    consumerRenderSpeed = marker.speed;
                    preciseTimeMs = marker.syncTime;

                    speedCheckpoints.poll();
                } else {
                    break;
                }
            }

            int remainingFrames = framesNeeded - framesProcessed;
            if (remainingFrames > 0) {
                preciseTimeMs += (remainingFrames * consumerRenderSpeed / data.sampleRate) * 1000.0;
            }

            lastUpdateTimeNano = System.nanoTime();
            interpolationSpeed = consumerRenderSpeed;
            if (preciseTimeMs > data.totalLengthMs) preciseTimeMs = data.totalLengthMs;
        } else {
            lastUpdateTimeNano = 0;
        }
    }

    public void play() {
        if (state.get() == PlayState.CLOSE) return;
        state.set(PlayState.PLAY);
        lastUpdateTimeNano = System.nanoTime();
        LockSupport.unpark(worker);
    }

    public void pause() {
        if (state.get() == PlayState.CLOSE) return;
        state.set(PlayState.PAUSE);
        lastUpdateTimeNano = 0;
    }

    public void stop() {
        if (state.get() == PlayState.CLOSE) return;
        state.set(PlayState.PAUSE);
        setTime(0);
        lastUpdateTimeNano = 0;
    }

    public void close() {
        state.set(PlayState.CLOSE);
        worker.running = false;
        worker.interrupt();
    }

    public void setTime(double timeMs) {
        long sample = (long) (timeMs / 1000.0 * data.sampleRate);
        sample = Math.max(0, Math.min(sample, data.samples.length / data.channels));
        if (sample % 2 != 0) sample--;

        seekTargetSample = sample;
        seekRequest = true;

        preciseTimeMs = timeMs;
        lastUpdateTimeNano = 0;

        // Seek 会清空缓冲区，所以直接重置消费端速度，无需 Marker
        speedCheckpoints.clear();
        consumerRenderSpeed = targetSpeed;

        speedChangeVersion.incrementAndGet();
        LockSupport.unpark(worker);
    }

    public double getTime() {
        if (state.get() != PlayState.PLAY || lastUpdateTimeNano == 0) {
            return preciseTimeMs;
        }
        long now = System.nanoTime();
        double deltaMs = ((now - lastUpdateTimeNano) / 1_000_000.0) * interpolationSpeed;
        return Math.min(preciseTimeMs + deltaMs, data.totalLengthMs);
    }

    public void setSpeed(double speed) {
        if (Math.abs(targetSpeed - speed) > 0.001) {
            targetSpeed = Math.max(0.1, speed);
            speedChangeVersion.incrementAndGet();
            LockSupport.unpark(worker);
        }
    }
    public double getSpeed() { return targetSpeed; }

    public void setPitch(double pitch) {
        if (Math.abs(targetPitch - pitch) > 0.001) {
            targetPitch = Math.max(0.1, pitch);
            speedChangeVersion.incrementAndGet();
            LockSupport.unpark(worker);
        }
    }
    public double getPitch() { return targetPitch; }

    public void setVolume(float v) { this.volume = Math.max(0f, Math.min(1f, v)); }
    public float getVolume() { return volume; }
    public void setPan(float p) { this.pan = Math.max(-1f, Math.min(1f, p)); }
    public void setMute(boolean m) { this.isMuted = m; }
    public boolean isMuted() { return isMuted; }
    public long getDuration() { return data.totalLengthMs; }
    public PlayState getPlayState() { return state.get(); }
    public void setEndBehavior(EndBehavior behavior) { this.endBehavior = behavior; }

    private class DSPWorker extends Thread {
        volatile boolean running = true;

        private volatile int sourceIndex = 0;
        private volatile double internalSpeed = 1.0;
        private volatile double internalPitch = 1.0;
        private int lastProcessedVersion = 0;

        private Sonic sonicPrimary;
        private final float[] inputChunk;
        private static final int CHUNK_FRAMES = 128;

        public DSPWorker() {
            super("Audio-DSP-Worker");
            setPriority(Thread.NORM_PRIORITY + 2);
            this.inputChunk = new float[CHUNK_FRAMES * data.channels];
            initSonic();
        }

        private void initSonic() {
            sonicPrimary = new Sonic(data.sampleRate, data.channels);
            sonicPrimary.setQuality(0);
            sonicPrimary.setSpeed((float) internalSpeed);
            sonicPrimary.setPitch((float) internalPitch);
        }

        @Override
        public void run() {
            while (running) {
                try {
                    if (seekRequest) {
                        handleSeek();
                        continue;
                    }

                    if (state.get() != PlayState.PLAY) {
                        LockSupport.park();
                        continue;
                    }

                    int currentVersion = speedChangeVersion.get();
                    if (currentVersion != lastProcessedVersion) {
                        handleSpeedChange(currentVersion);
                        continue;
                    }

                    boolean bypassSonic = (Math.abs(internalSpeed - 1.0) < 0.001 && Math.abs(internalPitch - 1.0) < 0.001);

                    int freeSpace = outputRingBuffer.capacity() - outputRingBuffer.size();
                    if (freeSpace < CHUNK_FRAMES * data.channels) {
                        LockSupport.parkNanos(500_000);
                        continue;
                    }

                    if (bypassSonic) {
                        int remaining = data.samples.length - sourceIndex;
                        if (remaining <= 0) {
                            handleEnd();
                            continue;
                        }

                        int writeCap = Math.min(freeSpace, 4096 * data.channels);
                        int toWrite = Math.min(writeCap, remaining);

                        outputRingBuffer.write(data.samples, sourceIndex, toWrite);
                        sourceIndex += toWrite;
                    } else {
                        if (sourceIndex >= data.samples.length && sonicPrimary.samplesAvailable() == 0) {
                            handleEnd();
                            continue;
                        }

                        feedSonic();
                        processOutput();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleSeek() {
            sourceIndex = (int) seekTargetSample * data.channels;

            initSonic();
            internalSpeed = targetSpeed;
            internalPitch = targetPitch;
            sonicPrimary.setSpeed((float) internalSpeed);
            sonicPrimary.setPitch((float) internalPitch);

            outputRingBuffer.clear();
            speedCheckpoints.clear();

            lastProcessedVersion = speedChangeVersion.get();
            seekRequest = false;
        }

        private void handleSpeedChange(int requestVersion) {
            double newSpeed = targetSpeed;
            double newPitch = targetPitch;

            if (speedChangeVersion.get() != requestVersion) return;

            int pendingSamples = outputRingBuffer.size();
            int pendingFrames = pendingSamples / data.channels;

            // 清空缓冲区并回滚 Source 索引，确保瞬时响应
            if (pendingFrames > 0) {
                double sourceFramesConsumed = pendingFrames / internalSpeed;
                int rollbackSamples = (int)(sourceFramesConsumed * data.channels);
                sourceIndex = Math.max(0, sourceIndex - rollbackSamples);

                outputRingBuffer.clear();
                speedCheckpoints.clear();
            }

            sonicPrimary.clear();

            internalSpeed = newSpeed;
            internalPitch = newPitch;
            sonicPrimary.setSpeed((float) internalSpeed);
            sonicPrimary.setPitch((float) internalPitch);
            sonicPrimary.setQuality(0);

            if (speedChangeVersion.get() != requestVersion) return;

            // 计算回滚后的准确物理时间
            double syncTime = ((double) sourceIndex / data.channels / data.sampleRate) * 1000.0;

            long writePos = outputRingBuffer.getTotalWritten();
            speedCheckpoints.offer(new SpeedMarker(writePos, internalSpeed, syncTime));

            lastProcessedVersion = requestVersion;
        }

        private void feedSonic() {
            int remaining = data.samples.length - sourceIndex;
            if (remaining <= 0) {
                sonicPrimary.flush();
                return;
            }
            int readLen = Math.min(CHUNK_FRAMES * data.channels, remaining);
            System.arraycopy(data.samples, sourceIndex, inputChunk, 0, readLen);
            sonicPrimary.write(inputChunk, readLen / data.channels);
            sourceIndex += readLen;
        }

        private void processOutput() {
            int framesToMake = CHUNK_FRAMES;
            float[] finalBuffer = new float[framesToMake * data.channels];
            int framesGenerated = sonicPrimary.read(finalBuffer, framesToMake);

            if (framesGenerated > 0) {
                outputRingBuffer.write(finalBuffer, 0, framesGenerated * data.channels);
            }
        }

        private void handleEnd() {
            if (endBehavior == EndBehavior.LOOP) {
                sourceIndex = 0;
                initSonic();
                outputRingBuffer.clear();
                preciseTimeMs = 0;
                speedCheckpoints.clear();
                consumerRenderSpeed = targetSpeed;
                lastProcessedVersion = speedChangeVersion.get();
            } else if (endBehavior == EndBehavior.PAUSE) {
                state.set(PlayState.PAUSE);
                stop();
            } else {
                state.set(PlayState.CLOSE);
                close();
            }
        }
    }
}