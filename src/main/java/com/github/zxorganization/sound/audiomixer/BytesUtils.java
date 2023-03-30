package com.github.zxorganization.sound.audiomixer;

import java.util.BitSet;

public class BytesUtils {

    public static String bytes2fStr(byte[] data) {
        String result = "";
        for (int i = 0; i < data.length; i++) {
            result += Integer.toHexString((data[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3) + " ";
        }
        return result;
    }

    public static short bytes2short(byte[] res) {
        short ret = 0;
        for (int i = 0; i < 2; i++) {
            if (i < res.length)
                ret |= (short) (res[i] & 0xFF) << (i * 8);
        }
        return ret;
    }

    public static int bytes2int(byte[] res) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            if (i < res.length)
                ret |= (res[i] & 0xFF) << (i * 8);
        }
        return ret;
    }

    public static long bytes2long(byte[] res) {
        long ret = 0;
        for (int i = 0; i < 8; i++) {
            if (i < res.length)
                ret |= (long) (res[i] & 0xFF) << (i * 8);
        }
        return ret;
    }

    public static float bytes2float(byte[] res) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            ret |= (res[i] & 0xFF) << (i * 8);
        }
        return Float.intBitsToFloat(ret);
    }

    public static double bytes2double(byte[] res) {
        long ret = 0;
        for (int i = 0; i < 8; i++) {
            ret |= (long) (res[i] & 0xFF) << (i * 8);
        }
        return Double.longBitsToDouble(ret);
    }

    public static byte byte2byteA(byte res) {
        BitSet ret = new BitSet(8);
        BitSet resb = BitSet.valueOf(new byte[]{res});
        for (int i = 0; i < 8; i++) {
            ret.set(i, resb.get(7 - i));
        }
        if (ret.length() == 0)
            return -128;
        return ret.toByteArray()[0];
    }

    public static byte[] bytes2bytesA(byte[] res) {
        byte[] ret = new byte[res.length];
        for (int i = 0; i < res.length; i++) {
            ret[i] = byte2byteA(res[i]);
        }
        return ret;
    }

    public static short bytes2shortA(byte[] res) {
        short ret = 0;
        for (int i = 0; i < 2; i++) {
            ret |= (short) (res[1 - i] & 0xFF) << (i * 8);
        }
        return ret;
    }

    public static byte[] bytesCut(byte[] res, int pos,int destPos, int destLen) {
        byte[] ret = new byte[destLen];
        System.arraycopy(res, pos, ret, destPos, destLen);
        return ret;
    }

    public static byte[] bytesSplicing(byte[] a, byte[] b) {
        byte[] ret = new byte[a.length + b.length];
        System.arraycopy(a, 0, ret, 0, a.length);
        System.arraycopy(b, 0, ret, a.length, b.length);
        return ret;
    }

    public static int bytes2intA(byte[] res) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            if (3 - i < res.length)
                ret |= (res[3 - i] & 0xFF) << (i * 8);
        }
        return ret;
    }

    public static long bytes2longA(byte[] res) {
        long ret = 0;
        for (int i = 0; i < 8; i++) {
            if (7 - i < res.length)
                ret |= (long) (res[7 - i] & 0xFF) << (i * 8);
        }
        return ret;
    }

    public static float bytes2floatA(byte[] res) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            ret |= (res[3 - i] & 0xFF) << (i * 8);
        }
        return Float.intBitsToFloat(ret);
    }

    public static double bytes2doubleA(byte[] res) {
        long ret = 0;
        for (int i = 0; i < 8; i++) {
            ret |= (long) (res[7 - i] & 0xFF) << (i * 8);
        }
        return Double.longBitsToDouble(ret);
    }


    public static byte[] longs2bytesA(long[] res) {
        byte[] ret = new byte[res.length * 8];

        for (int i = 0; i < res.length; i++) {

            for (int j = 0; j < 8; j++) {//字节
                ret[j + i * 8] = long2bytes(res[i])[7 - j];
            }

        }

        return ret;
    }


    public static byte[] short2bytes(short res) {
        byte[] ret = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = 16 - (i + 1) * 8;
            ret[i] = (byte) ((res >> offset) & 0xff);
        }
        return ret;
    }

    public static byte[] int2bytes(int res) {
        byte[] ret = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = 32 - (i + 1) * 8;
            ret[i] = (byte) ((res >> offset) & 0xff);
        }
        return ret;
    }

    public static byte[] long2bytes(long res) {
        byte[] ret = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            ret[i] = (byte) ((res >> offset) & 0xff);
        }
        return ret;
    }

    public static byte[] float2bytes(float res) {
        return int2bytes(Float.floatToIntBits(res));
    }

    public static byte[] short2bytesA(short res) {
        byte[] buffer = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = 16 - (1 - i + 1) * 8;
            buffer[i] = (byte) ((res >> offset) & 0xff);
        }
        return buffer;
    }

    public static byte[] int2bytesA(int res) {
        byte[] buffer = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = 32 - (3 - i + 1) * 8;
            buffer[i] = (byte) ((res >> offset) & 0xff);
        }
        return buffer;
    }

    public static byte[] long2bytesA(long res) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (7 - i + 1) * 8;
            buffer[i] = (byte) ((res >> offset) & 0xff);
        }
        return buffer;
    }

    public static byte[] double2bytes(double res) {
        return long2bytes(Double.doubleToLongBits(res));
    }

    public static byte[] float2bytesA(float res) {
        return int2bytesA(Float.floatToIntBits(res));
    }

    public static byte[] double2bytesA(double res) {
        return long2bytesA(Double.doubleToLongBits(res));
    }


    public static BitSet bytes2bits(byte[] res) {
        BitSet ret = new BitSet(res.length * 8);
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < 8; j++) {
                ret.set(i * 8 + j, ((res[i] >> j) & 0x01) == 1);
            }
        }
        return ret;
    }

    public static BitSet bytes2bitsA(byte[] res) {
        BitSet ret = new BitSet(res.length * 8);
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < 8; j++) {
                ret.set(res.length * 8 - i * 8 - j - 1, ((res[i] >> j) & 0x01) == 1);
            }
        }
        return ret;
    }
}
