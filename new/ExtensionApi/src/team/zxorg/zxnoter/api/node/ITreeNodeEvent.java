package team.zxorg.zxnoter.api.node;

import team.zxorg.zxnoter.api.core.IEvent;
import team.zxorg.zxnoter.api.core.IEventListener;

public interface ITreeNodeEvent<T> extends IEvent {
    void onValueChanged(T oldValue, T newValue);

    void onChildAdded(T value);

    void onChildRemoved(T value);
}
