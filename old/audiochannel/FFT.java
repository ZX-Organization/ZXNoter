package team.zxorg.zxnoter.audiochannel;

public class FFT {
    /**
     * 对复数数组执行快速傅里叶变换（FFT）。
     * 输入数组将被原地修改。
     *
     * @param x 复数数组的输入
     */
    public static void fft(Complex[] x) {
        int n = x.length;

        if (n <= 1) {
            return;
        }

        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];

        for (int i = 0; i < n / 2; i++) {
            even[i] = x[2 * i];
            odd[i] = x[2 * i + 1];
        }

        fft(even);
        fft(odd);

        for (int k = 0; k < n / 2; k++) {
            double angle = -2 * k * Math.PI / n;
            Complex exp = new Complex(Math.cos(angle), Math.sin(angle));
            Complex t = exp.times(odd[k]);
            x[k] = even[k].plus(t);
            x[k + n / 2] = even[k].minus(t);
        }
    }
    /**
     * 对复数数组执行逆快速傅里叶变换（IFFT）。
     * 输入数组将被原地修改。
     *
     * @param x 复数数组的输入
     */
    public static void ifft(Complex[] x) {
        int n = x.length;

        for (int i = 0; i < n; i++) {
            x[i] = x[i].conjugate();
        }

        fft(x);

        double scaleFactor = 1.0 / Math.sqrt(n);
        for (int i = 0; i < n; i++) {
            x[i] = new Complex(x[i].re() * scaleFactor, x[i].im() * scaleFactor);
        }
    }
}