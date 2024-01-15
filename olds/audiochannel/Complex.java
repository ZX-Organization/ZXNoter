package team.zxorg.extensionloader.audiochannel;
/**
 * 复数类，包含实部和虚部。
 */
public class Complex {
    private final double real;
    private final double imaginary;

    /**
     * 构造一个复数对象。
     *
     * @param real      实部
     * @param imaginary 虚部
     */
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * 获取实部。
     *
     * @return 实部
     */
    public double re() {
        return real;
    }

    /**
     * 获取虚部。
     *
     * @return 虚部
     */
    public double im() {
        return imaginary;
    }

    /**
     * 复数加法。
     *
     * @param other 另一个复数对象
     * @return 相加后的复数结果
     */
    public Complex plus(Complex other) {
        return new Complex(real + other.real, imaginary + other.imaginary);
    }

    /**
     * 复数减法。
     *
     * @param other 另一个复数对象
     * @return 相减后的复数结果
     */
    public Complex minus(Complex other) {
        return new Complex(real - other.real, imaginary - other.imaginary);
    }

    /**
     * 复数乘法。
     *
     * @param other 另一个复数对象
     * @return 相乘后的复数结果
     */
    public Complex times(Complex other) {
        double realPart = real * other.real - imaginary * other.imaginary;
        double imagPart = real * other.imaginary + imaginary * other.real;
        return new Complex(realPart, imagPart);
    }

    /**
     * 求共轭复数。
     *
     * @return 共轭复数结果
     */
    public Complex conjugate() {
        return new Complex(real, -imaginary);
    }
}