package net.thiccaxe.resonance.network;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;

public class WebSocketConnection {

    private final SocketChannel channel;

    private SocketAddress remote;

    public WebSocketConnection(@NotNull SocketChannel channel) {
        this.channel = channel;
        this.remote = channel.remoteAddress();
    }

    public void disconnect() {
        this.channel.close();
    }

    @NotNull
    public Channel getChannel() {
        return channel;
    }

}
