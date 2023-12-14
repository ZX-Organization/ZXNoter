package team.zxorg.engine2d.engine3d;

public class Vector3D {
    private double x, y, z;

    public Vector3D(double xyz) {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
    }

    public Vector3D(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector3D getVector() {
        return this;
    }

    public void setVector(Vector3D vector) {
        setTo(vector);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * 设置为
     * @param source 源向量
     */
    public void setTo(Vector3D source) {
        x = source.x;
        y = source.y;
        z = source.z;
    }


    /**
     * 将向量相加
     *
     * @param result  计算结果
     * @param vectors 欲计算的向量数组
     */
    public static void add(Vector3D result, Vector3D... vectors) {
        result.setToZero();
        for (Vector3D vector : vectors) {
            result.x += vector.x;
            result.y += vector.y;
            result.z += vector.z;
        }
    }

    /**
     * 将向量相减
     *
     * @param result  计算结果
     * @param vectors 欲计算的向量数组
     */
    public static void subtract(Vector3D result, Vector3D... vectors) {
        result.setToZero();
        for (Vector3D vector : vectors) {
            result.x -= vector.x;
            result.y -= vector.y;
            result.z -= vector.z;
        }
    }

    /**
     * 将向量相乘
     *
     * @param result  计算结果
     * @param vectors 欲计算的向量数组
     */
    public static void multiply(Vector3D result, Vector3D... vectors) {
        result.setToIdentity();
        for (Vector3D vector : vectors) {
            result.x *= vector.x;
            result.y *= vector.y;
            result.z *= vector.z;
        }
    }

    public void setVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 将向量和此向量相加
     *
     * @param vector 欲计算的向量
     */
    public void add(Vector3D vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public void add(Vector3D... vectors) {
        for (Vector3D vector : vectors) {
            x += vector.x;
            y += vector.y;
            z += vector.z;
        }
    }

    /**
     * 将向量和此向量相减
     *
     * @param vector 欲计算的向量
     */
    public void subtract(Vector3D vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
    }

    /**
     * 将向量和此向量相减
     *
     * @param vectors 欲计算的向量数组
     */
    public void subtract(Vector3D... vectors) {
        for (Vector3D vector : vectors) {
            x -= vector.x;
            y -= vector.y;
            z -= vector.z;
        }
    }

    /**
     * 将向量和此向量相乘
     *
     * @param vector 欲计算的向量
     */
    public void multiply(Vector3D vector) {
        x *= vector.x;
        y *= vector.y;
        z *= vector.z;
    }

    /**
     * 将向量和此向量相乘
     *
     * @param vectors 欲计算的向量数组
     */
    public void multiply(Vector3D... vectors) {
        for (Vector3D vector : vectors) {
            x *= vector.x;
            y *= vector.y;
            z *= vector.z;
        }
    }

    /**
     * 将向量和此向量相除
     *
     * @param vector 欲计算的向量
     */
    public void divide(Vector3D vector) {
        x /= vector.x;
        y /= vector.y;
        z /= vector.z;
    }

    /**
     * 将向量和此向量相除
     *
     * @param vectors 欲计算的向量数组
     */
    public void divide(Vector3D... vectors) {
        for (Vector3D vector : vectors) {
            x /= vector.x;
            y /= vector.y;
            z /= vector.z;
        }
    }



    /**
     * 将此向量设为零
     */
    public void setToZero() {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * 将此向量设为单位向量（1, 1, 1）
     */
    public void setToIdentity() {
        x = 1;
        y = 1;
        z = 1;
    }

    /**
     * 翻转此向量的 x、y、z 值
     */
    public void flip() {
        x = -x;
        y = -y;
        z = -z;
    }
}
