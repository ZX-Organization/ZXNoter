package team.zxorg.zxncore.test.fft;

public class Complex {
    public double i;
    public double j;// 虚数
    public Complex(double i, double j) {
        this.i = i;
        this.j = j;
    }

    public double getMod() {// 求复数的模
        return Math.sqrt(i * i + j * j);
    }

    public static Complex Add(Complex a, Complex b) {
        return new Complex(a.i + b.i, a.j + b.j);
    }

    public static Complex Subtract(Complex a, Complex b) {
        return new Complex(a.i - b.i, a.j - b.j);
    }

    public static Complex Mul(Complex a, Complex b) {// 乘法
        return new Complex(a.i * b.i - a.j * b.j, a.i * b.j + a.j * b.i);
    }

    public static Complex GetW(int k, int N) {
        return new Complex(Math.cos(-2 * Math.PI * k / N), Math.sin(-2 * Math.PI * k / N));
    }

    public static Complex[] butterfly(Complex a, Complex b, Complex w) {
        return new Complex[] { Add(a, Mul(w, b)), Subtract(a, Mul(w, b)) };
    }

    public static Double[] toModArray(Complex[] complex) {
        Double[] res = new Double[complex.length];
        for (int i = 0; i < complex.length; i++) {
            res[i] = complex[i].getMod();
        }
        return res;
    }

}