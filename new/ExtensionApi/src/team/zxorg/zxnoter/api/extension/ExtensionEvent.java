package team.zxorg.zxnoter.api.extension;

public class ExtensionEvent {
    public static final EventType<ExtensionEvent> MOUSE_CLICKED = new EventType<>(MouseEvent.ANY, "MOUSE_CLICKED");
}
