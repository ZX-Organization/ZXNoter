package team.zxorg.zxnoter.uiframe.command.parameter;

public class StringParameter implements Parameter<String> {
    @Override
    public String parse(String parameter) {
        return parameter;
    }
}
