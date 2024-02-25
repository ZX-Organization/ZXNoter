package team.zxorg.zxnoter.ui.command.parameter;

import team.zxorg.zxnoter.ui.behavior.Behavior;

import java.util.List;

public class ParametersBehavior {

    private final Behavior behavior;
    private final List<Class<? extends Parameter>> args;

    public ParametersBehavior(Behavior behavior, List<Class<? extends Parameter>> args) {
        this.behavior = behavior;
        this.args = args;
    }
}
