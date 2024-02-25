package team.zxorg.zxnoter.ui.command.parameter;

public class StringParameter implements Parameter<String> {
    @Override
    public String parse(String parameter) {
        return parameter;
    }
}
