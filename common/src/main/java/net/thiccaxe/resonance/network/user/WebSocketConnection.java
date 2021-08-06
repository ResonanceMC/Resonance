package net.thiccaxe.resonance.network.user;


import net.thiccaxe.resonance.ConnectedUser;
import net.thiccaxe.resonance.network.ConnectionState;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;

public class WebSocketConnection {


    private volatile ConnectionState connectionState;

    private ConnectedUser connectedUser;
    private boolean online;



    public WebSocketConnection() {
        super();
        this.online = true;
        this.connectionState = ConnectionState.UNKNOWN;
    }

    public @NotNull ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(@NotNull ConnectionState connectionState) {
        this.connectionState = connectionState;
    }
}
