package me.faintcloudy.advantagebackdoor.api.message.play.client;

import com.alibaba.fastjson.annotation.JSONField;
import me.faintcloudy.advantagebackdoor.api.connection.UserConnection;
import me.faintcloudy.advantagebackdoor.api.message.SimpleNetworkMessage;

public abstract class ClientNetworkMessage extends SimpleNetworkMessage {

    @JSONField
    private UserConnection user;

    public UserConnection getUser() {
        return user;
    }

    public void setUser(UserConnection user) {
        this.user = user;
    }

}
