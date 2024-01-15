package team.zxorg.engine2d.engine3d.component;

import team.zxorg.engine2d.engine3d.Vector3D;

/**
 * 变换组件
 */
public class Transform extends Component {
    /**
     * 变换的世界空间位置
     */
    public final Vector3D position = new Vector3D(0) {
        @Override
        public void setVector(Vector3D vector) {
            if (parent != null) {
                //如果有父 则重新计算相对父位置
                localPosition.setTo(position);
                localPosition.subtract(parent.position);
            }
            super.setVector(vector);
        }
    };
    /**
     * 变换相对于父变换的位置
     */
    public final Vector3D localPosition = new Vector3D(0) {
        @Override
        public void setVector(Vector3D vector) {
            //计算世界坐标位置
            position.setTo(parent.position);
            position.add(vector);
            super.setVector(vector);
        }
    };
    /**
     * 缩放向量
     */
    public final Vector3D scale = new Vector3D(1) {
        @Override
        public void setVector(Vector3D vector) {
            // 计算世界坐标缩放
            setTo(parent.scale);
            multiply(vector);
            super.setVector(vector);
        }
    };
    /**
     * 相对于父级的变换比例
     */
    public final Vector3D localScale = new Vector3D(1) {
        @Override
        public void setVector(Vector3D vector) {
            // 计算相对父级的本地缩放
            setTo(vector);
            super.setVector(vector);
        }
    };

    /**
     * 旋转向量
     */
    public final Vector3D rotation = new Vector3D(0) {
        @Override
        public void setVector(Vector3D vector) {
            // 计算世界坐标旋转
            setTo(parent.rotation);
            add(vector);
            super.setVector(vector);
        }
    };
    /**
     * 变换相对于父级变换旋转的旋转
     */
    public final Vector3D localRotation = new Vector3D(0) {
        @Override
        public void setVector(Vector3D vector) {
            // 计算相对父级的本地旋转
            setTo(vector);
            super.setVector(vector);
        }
    };

    /**
     * 父变换组件
     */
    private Transform parent;

    /**
     * 设置父变换
     *
     * @param parent             父变换组件
     * @param worldPositionStays 如果是true，则保持世界坐标，本地坐标变动
     */
    public void setParent(Transform parent, boolean worldPositionStays) {
        if (this.parent != parent) {
            // 检测循环依赖
            if (isAncestor(parent)) {
                throw new IllegalArgumentException("循环依赖错误！");
            }

            if (worldPositionStays) {
                // 保持世界坐标，计算本地坐标
                if (parent != null) {
                    localPosition.setTo(position);
                    localPosition.subtract(parent.position);
                } else {
                    // 没有父变换时，世界坐标等同于本地坐标
                    localPosition.setTo(position);
                }
            } else {
                // 保持本地坐标，计算世界坐标
                if (parent != null) {
                    position.setTo(localPosition);
                    position.add(parent.position);
                } else {
                    // 没有父变换时，本地坐标等同于世界坐标
                    localPosition.setTo(position);
                }
            }
            this.parent = parent;
        }
    }

    /**
     * 检测是否是祖先节点（避免循环依赖）
     */
    private boolean isAncestor(Transform potentialAncestor) {
        Transform ancestor = this.parent;
        while (ancestor != null) {
            if (ancestor == potentialAncestor) {
                return true;
            }
            ancestor = ancestor.parent;
        }
        return false;
    }

    /**
     * 获取父变换
     *
     * @return 变换组件
     */
    public Transform getParent() {
        return parent;
    }

    public Transform() {
    }

    /**
     * 坐标变换
     *
     * @param positionDelta 坐标向量增量
     */
    public void position(Vector3D positionDelta) {
        position.add(positionDelta);
    }

    /**
     * 旋转变换
     *
     * @param rotationDelta 旋转向量增量
     */
    public void rotate(Vector3D rotationDelta) {
        rotation.add(rotationDelta);
    }

    /**
     * 缩放变换
     *
     * @param scalingFactor 缩放向量增量
     */
    public void scale(Vector3D scalingFactor) {
        scale.multiply(scalingFactor);
    }
}
