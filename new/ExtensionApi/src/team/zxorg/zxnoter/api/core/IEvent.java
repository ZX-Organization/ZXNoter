package team.zxorg.zxnoter.api.core;

public interface IEvent {
    /**
     * 获取事件源
     *
     * @return 事件源
     */
    Object getEventSource();

    /**
     * 设置事件源
     *
     * @param eventSource 事件源
     */
    void setEventSource(Object eventSource);

}
