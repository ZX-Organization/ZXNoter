package team.zxorg.engine2d.engine3d.component;

import team.zxorg.engine2d.engine3d.ElementObject;

public abstract class Component {
    /**
     * 附加到此元素的转换
     */
    Transform transform;
    /**
     * 该组件附加到的元素对象
     */
    ElementObject elementObject;
}
