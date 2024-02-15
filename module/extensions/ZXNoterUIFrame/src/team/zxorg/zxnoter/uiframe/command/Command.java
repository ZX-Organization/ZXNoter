package team.zxorg.zxnoter.uiframe.command;

import team.zxorg.zxnoter.uiframe.command.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;

public class Command {
    /**
     * 子命令
     */
    private final List<Command> subcommands = new ArrayList<>();
    /**
     * 指令名
     */
    private final List<String> name = new ArrayList<>();
    /**
     * 指令接收的参数
     */
    private final List<List<Class<? extends Parameter>>> argsList = new ArrayList<>();

    /**
     * 指令提示器
     */
    private final CommandPrompter commandPrompter = new CommandPrompter();
    /**
     * 指令补全器
     */
    private final CommandCompleter commandCompleter = new CommandCompleter();

    /**
     * 添加子命令
     *
     * @param subcommands 子命令
     */
    public void addSubcommand(Command... subcommands) {
        this.subcommands.addAll(List.of(subcommands));
    }

    public Command(String name) {
        this.name.add(name);
    }


}
