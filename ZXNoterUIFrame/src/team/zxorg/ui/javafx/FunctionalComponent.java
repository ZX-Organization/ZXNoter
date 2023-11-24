package team.zxorg.ui.javafx;

import javafx.scene.Node;

/**
 * 布局组件
 */
public abstract class FunctionalComponent {
    @Override
    public String toString() {
        return "FunctionalComponent{" +
                "fullDomain='" + fullDomain + '\'' +
                '}';
    }

    /**
     * 根对象 负责提供组件之间的通讯控制
     */
    protected final ProjectView projectView;
    /**
     * 子域
     */
    protected final String thisDomain;
    /**
     * 完整域
     */
    protected final String fullDomain;
    /**
     * 父组件
     */
    protected final FunctionalComponent parent;


    public FunctionalComponent(ProjectView projectView, FunctionalComponent parent, String thisDomain) {
        this.projectView = projectView;
        this.thisDomain = thisDomain;
        this.parent = parent;

        StringBuilder sb = new StringBuilder();
        sb.append(thisDomain);
        FunctionalComponent p = parent;
        if (parent != null)
            while (true) {
                if (p != null) {
                    sb.insert(0, ".").insert(0, p.thisDomain);
                    p = p.parent;
                } else {
                    break;
                }
            }
        fullDomain = sb.toString();
    }

    /**
     * 获取节点
     */
    public abstract Node getNode();

}