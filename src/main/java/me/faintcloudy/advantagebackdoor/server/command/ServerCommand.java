package me.faintcloudy.advantagebackdoor.server.command;

public abstract class ServerCommand {
    protected String name;
    protected String introduce;
    public ServerCommand(String name, String introduce)
    {
        this.name = name;
        this.introduce = introduce;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(String[] args);

}
