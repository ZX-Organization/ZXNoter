package team.zxorg.zxnoter.sound.audiomixer;

import java.util.concurrent.atomic.AtomicLong;

public class RingBuffer {
    private final float[] buffer;
    private final int capacity;
    private final int mask;

    private final AtomicLong writePos = new AtomicLong(0);
    private final AtomicLong readPos = new AtomicLong(0);

    public RingBuffer(int capacity) {
        int cap = 1;
        while (cap < capacity) cap <<= 1;
        this.capacity = cap;
        this.mask = cap - 1;
        this.buffer = new float[this.capacity];
    }

    public int write(float[] src, int offset, int length) {
        long currentRead = readPos.get();
        long currentWrite = writePos.get();
        int available = capacity - (int)(currentWrite - currentRead);

        if (available <= 0) return 0;

        int toWrite = Math.min(length, available);
        int writeIndex = (int)(currentWrite & mask);

        int firstChunk = Math.min(toWrite, capacity - writeIndex);
        System.arraycopy(src, offset, buffer, writeIndex, firstChunk);

        if (firstChunk < toWrite) {
            System.arraycopy(src, offset + firstChunk, buffer, 0, toWrite - firstChunk);
        }

        writePos.lazySet(currentWrite + toWrite);
        return toWrite;
    }

    public int read(float[] dst, int offset, int length) {
        long currentWrite = writePos.get();
        long currentRead = readPos.get();
        int available = (int)(currentWrite - currentRead);

        if (available <= 0) return 0;

        int toRead = Math.min(length, available);
        int readIndex = (int)(currentRead & mask);

        int firstChunk = Math.min(toRead, capacity - readIndex);
        System.arraycopy(buffer, readIndex, dst, offset, firstChunk);

        if (firstChunk < toRead) {
            System.arraycopy(buffer, 0, dst, offset + firstChunk, toRead - firstChunk);
        }

        readPos.lazySet(currentRead + toRead);
        return toRead;
    }

    public int size() {
        return (int)(writePos.get() - readPos.get());
    }

    public int capacity() {
        return capacity;
    }

    public void clear() {
        // 彻底重置：将读写指针同步
        // 注意：在多线程环境下调用 clear 通常意味着 "Seek" 或 "Stop"，
        // 此时应确保没有并发的 read/write 调用，或者即使有，产生的错乱数据也是可以接受的（因为马上要重填）。
        long wp = writePos.get();
        readPos.set(wp);
    }

    public long getTotalWritten() {
        return writePos.get();
    }

    public long getTotalRead() {
        return readPos.get();
    }
}