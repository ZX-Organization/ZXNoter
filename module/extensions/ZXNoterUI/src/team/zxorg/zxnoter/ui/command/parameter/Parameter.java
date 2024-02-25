package team.zxorg.zxnoter.ui.command.parameter;

public interface Parameter<T> {
    T parse(String parameter);
}
