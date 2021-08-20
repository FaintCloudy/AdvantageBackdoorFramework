package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;

import java.util.ArrayList;
import java.util.List;

public class CommandListCommand extends ServerCommand {
    public CommandListCommand() {
        super("/?", "查看指令列表");
    }

    @Override
    public void execute(String[] args) {
        List<String> list = new ArrayList<>();
        for (ServerCommand command : Log4jServer.getInstance().getCommandController().getServerCommands()) {
            list.add(command.name + " - " + command.introduce);
        }
        for (String s : list) {
            Log4jServer.getInstance().getLogger().info(s);
        }
    }
}
