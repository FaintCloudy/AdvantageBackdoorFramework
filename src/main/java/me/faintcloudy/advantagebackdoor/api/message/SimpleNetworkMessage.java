package me.faintcloudy.advantagebackdoor.api.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.nio.charset.StandardCharsets;

public class SimpleNetworkMessage implements NetworkMessage {

    @JSONField
    public Class<? extends NetworkMessage> theClass = this.getClass();

    @Override
    public byte[] serialize() {
        String jsonString = JSON.toJSONString(this);
        return jsonString.getBytes(StandardCharsets.UTF_8);
    }

}
