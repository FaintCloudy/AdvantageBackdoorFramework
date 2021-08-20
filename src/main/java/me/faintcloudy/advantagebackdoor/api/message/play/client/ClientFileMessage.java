package me.faintcloudy.advantagebackdoor.api.message.play.client;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

public class ClientFileMessage extends ClientNetworkMessage {
    @JSONField
    public byte[] file;
    @JSONField
    public UUID uuid;
    @JSONField
    public boolean end;
    @JSONField
    public String name;

    public ClientFileMessage(byte[] file, UUID uuid, boolean end, String name)
    {
        this.file = file;
        this.uuid = uuid;
        this.end = end;
        this.name = name;
    }

    public ClientFileMessage() {}
}
