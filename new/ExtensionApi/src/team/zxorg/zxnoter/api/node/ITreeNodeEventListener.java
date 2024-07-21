package team.zxorg.zxnoter.api.node;

public interface ITreeNodeEventListener<T> {
    void onValueChanged(T oldValue, T newValue);

    void onChildAdded(T value);

    void onChildRemoved(T value);
}
