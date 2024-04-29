package team.zxorg.zxnoter.ui.command;

import team.zxorg.zxnoter.ui.command.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * 指令参数
 */
public class CommandParameter {
    private final List<Class<? extends Parameter<?>>> argsList = new ArrayList<>();
    private final Command command;

    public CommandParameter(Command command) {
        this.command = command;
    }
}