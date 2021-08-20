package me.faintcloudy.advantagebackdoor.api.message.play.client;

import com.alibaba.fastjson.annotation.JSONField;

public class FallbackMessage extends ClientNetworkMessage {
    @JSONField
    public String fallback;
    public FallbackMessage(String fallback)
    {
        this.fallback = fallback;
    }

    public FallbackMessage() {}

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }

    public String getFallback() {
        return fallback;
    }
}

