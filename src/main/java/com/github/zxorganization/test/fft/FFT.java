package com.github.zxorganization.test.fft;

public class FFT{
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
            Complex[] res = Complex.butterfly(input[0], input[1], Complex.GetW(0, N));
            return res;
        }
    }
}
