package net.thiccaxe.resonance.network.player;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import net.thiccaxe.resonance.network.ConnectionState;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;

public class NettyWebSocketConnection {

    private final SocketChannel channel;

    private final SocketAddress remote;
    private ConnectionState connectionState;

    public NettyWebSocketConnection(@NotNull SocketChannel channel) {
        this.channel = channel;
        this.remote = channel.remoteAddress();
        connectionState = ConnectionState.UNKNOWN;
    }

    public void disconnect() {
        this.channel.close();
    }

    @NotNull
    public Channel getChannel() {
        return channel;
    }

    @NotNull
    public ConnectionState getConnectionState() {
        return connectionState;
    }
}
