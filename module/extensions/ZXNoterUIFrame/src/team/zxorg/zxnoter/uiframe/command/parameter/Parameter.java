package team.zxorg.zxnoter.uiframe.command.parameter;

public interface Parameter<T> {
    T parse(String parameter);
}
