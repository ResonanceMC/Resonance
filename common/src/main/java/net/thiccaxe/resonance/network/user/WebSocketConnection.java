package net.thiccaxe.resonance.network.user;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import net.thiccaxe.resonance.ConnectedUser;
import net.thiccaxe.resonance.network.ConnectionState;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;

public class WebSocketConnection {

    private final SocketChannel channel;

    private final SocketAddress remote;
    private volatile ConnectionState connectionState;

    private ConnectedUser connectedUser;
    private boolean online;



    public WebSocketConnection(@NotNull SocketChannel channel) {
        super();
        this.channel = channel;
        this.remote = channel.remoteAddress();
        this.online = true;
        this.connectionState = ConnectionState.UNKNOWN;
    }

    public @NotNull SocketAddress getRemote() {
        return remote;
    }

    public void disconnect() {
        this.channel.close();
    }

    @NotNull
    public Channel getChannel() {
        return channel;
    }

    public @NotNull ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(@NotNull ConnectionState connectionState) {
        this.connectionState = connectionState;
    }
}
