package team.zxorg.zxnoter.api.core;

public interface IEventHandler<T extends IEvent> {
    void handleEvent(T event);

    void addEventListener(IEventListener<T> listener);

    void removeEventListener(IEventListener<T> listener);
}
