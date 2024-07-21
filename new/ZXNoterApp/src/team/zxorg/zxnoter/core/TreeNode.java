package team.zxorg.zxnoter.core;

import org.jetbrains.annotations.NotNull;
import team.zxorg.zxnoter.api.node.ITreeNode;

import java.util.*;
import java.util.function.Predicate;

public class TreeNode implements ITreeNode {
    private final String name;
    private final Map<String, ITreeNode> children = new HashMap<>();
    private final ITreeNode parent;
    private Object value;

    protected TreeNode(String name, ITreeNode parent) {
        this.name = sanitizeKey(name);
        this.parent = parent;
    }


    public TreeNode(String name) {
        this.name = name;
        this.parent = null;
    }

    private static String sanitizeKey(String name) {
        return name.toLowerCase(Locale.ROOT).replaceAll("\\.", "");
    }

    @Override
    public <T> T getValue(@NotNull Class<T> tclass) {
        return tclass.cast(value);
    }

    @Override
    public <T> void setValue(@NotNull T value) {
        this.value = value;
    }

    @Override
    public ITreeNode getParentNode() {
        return parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ITreeNode addNode(@NotNull String name) {
        return children.computeIfAbsent(sanitizeKey(name), s -> new TreeNode(s, this));
    }

    @Override
    public ITreeNode getNode(@NotNull String name) {
        return children.get(sanitizeKey(name));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof ITreeNode key)
            return Objects.equals(getPath(), key.getPath());
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }

    @Override
    public String toString() {
        return getPath();
    }

    @Override
    public String getPath() {
        List<String> path = new LinkedList<>();
        ITreeNode current = this;
        while (current != null) {
            path.addFirst(current.getName());
            current = current.getParentNode();
        }
        return String.join(".", path);
    }

    @Override
    public Collection<ITreeNode> getChildren() {
        return Collections.unmodifiableCollection(children.values());
    }

    public ITreeNode findByName(String name) {
        if (name == null) {
            return null;
        }
        // 先检查当前节点的子节点
        ITreeNode node = children.get(sanitizeKey(name));
        if (node != null) {
            return node;
        }
        // 递归地在子节点中查找
        for (ITreeNode child : children.values()) {
            ITreeNode result = ((TreeNode) child).findByName(name);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public List<ITreeNode> findByCondition(Predicate<String> condition) {
        List<ITreeNode> result = new ArrayList<>();
        if (condition.test(name)) {
            result.add(this);
        }
        for (ITreeNode child : children.values()) {
            result.addAll(child.findByCondition(condition));
        }
        return result;
    }


}
