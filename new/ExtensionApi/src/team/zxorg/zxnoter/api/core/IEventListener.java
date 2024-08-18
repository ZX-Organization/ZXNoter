package team.zxorg.zxnoter.api.core;

public interface IEventListener<T extends IEvent> {
    void onEvent(T event);
}
