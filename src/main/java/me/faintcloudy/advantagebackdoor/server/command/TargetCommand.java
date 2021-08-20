package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;

public class TargetCommand extends BranchedCommand {
    public TargetCommand() {
        super("/target", "操作当前指令目标");
    }

    @SubCommand(value = "list", usage = "/target list", introduce = "查看当前目标")
    public void listTarget()
    {
        Log4jServer.getInstance().getLogger().info("当前目标: ");
        if (Log4jServer.getInstance().getTargets().isEmpty())
        {
            Log4jServer.getInstance().getLogger().info("空!");
            return;
        }
        for (UserConnection target : Log4jServer.getInstance().getTargets()) {
            Log4jServer.getInstance().getLogger().info(target.getId() + "号 (" + target.getAddress() + "): " + target.getOS());
        }
    }

    @SubCommand(value = "add", usage = "/target add <用户 ID>", introduce = "添加目标")
    public void addTarget(String[] args)
    {
        if (args.length < 3)
        {
            Log4jServer.getInstance().getLogger().info("正确用法: /target add <用户 ID>");
            return;
        }
        if (!args[2].matches("[0-9]*"))
        {
            Log4jServer.getInstance().getLogger().info("用户 ID 必须为数字");
            return;
        }
        int id = Integer.parseInt(args[2]);
        UserConnection user = null;
        for (UserConnection u : Log4jServer.getInstance().getUsers()) {
            if (u.getId() == id)
            {
                user = u;
                break;
            }
        }
        if (user == null)
        {
            Log4jServer.getInstance().getLogger().info("找不到该客户");
            return;
        }
        if (Log4jServer.getInstance().getTargets().contains(user))
        {
            Log4jServer.getInstance().getLogger().info("该用户已经在目标列表里, 无法重复添加");
            return;
        }

        Log4jServer.getInstance().getTargets().add(user);
        Log4jServer.getInstance().getLogger().info("成功将 " + user + "添加到目标列表");
    }

    @SubCommand(value = "remove", usage = "/target remove <用户 ID>", introduce = "添加目标")
    public void removeTarget(String[] args)
    {
        if (args.length < 3)
        {
            Log4jServer.getInstance().getLogger().info("正确用法: /target remove <用户 ID>");
            return;
        }
        if (!args[1].matches("[0-9]*"))
        {
            Log4jServer.getInstance().getLogger().info("用户 ID 必须为数字");
            return;
        }
        int id = Integer.parseInt(args[2]);
        UserConnection user = null;
        for (UserConnection u : Log4jServer.getInstance().getUsers()) {
            if (u.getId() == id)
            {
                user = u;
                break;
            }
        }
        if (user == null)
        {
            Log4jServer.getInstance().getLogger().info("找不到该客户");
            return;
        }
        if (!Log4jServer.getInstance().getTargets().contains(user))
        {
            Log4jServer.getInstance().getLogger().info("该用户已不在目标列表里, 无法重复移除");
            return;
        }

        Log4jServer.getInstance().getTargets().add(user);
        Log4jServer.getInstance().getLogger().info("成功将 " + user + " 从目标列表移除");
    }

    @SubCommand(value = "tga", usage = "/target tga", introduce = "将所有用户添加至目标列表")
    public void targetAll()
    {
        for (UserConnection user : Log4jServer.getInstance().getUsers())
        {
            if (Log4jServer.getInstance().getTargets().contains(user))
                continue;
            Log4jServer.getInstance().getTargets().add(user);
            Log4jServer.getInstance().getLogger().info("成功将 " + user + " 添加到目标列表");
        }
    }

    @SubCommand(value = "set", usage = "/target set <用户 ID>", introduce = "设置单一目标")
    public void setTarget(String[] args)
    {
        if (args.length < 3)
        {
            Log4jServer.getInstance().getLogger().info("正确用法: /target set <用户 ID>");
            return;
        }
        if (!args[2].matches("[0-9]*"))
        {
            Log4jServer.getInstance().getLogger().info("用户 ID 必须为数字");
            return;
        }
        int id = Integer.parseInt(args[2]);
        UserConnection user = null;
        for (UserConnection u : Log4jServer.getInstance().getUsers()) {
            if (u.getId() == id)
            {
                user = u;
                break;
            }
        }
        if (user == null)
        {
            Log4jServer.getInstance().getLogger().info("找不到该客户");
            return;
        }
        Log4jServer.getInstance().getTargets().clear();
        Log4jServer.getInstance().getTargets().add(user);

        Log4jServer.getInstance().getLogger().info("成功将 " + user + " 设置为当前目标");
    }
}
