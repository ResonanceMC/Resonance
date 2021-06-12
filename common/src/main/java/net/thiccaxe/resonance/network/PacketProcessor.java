package net.thiccaxe.resonance.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketProcessor {

    private final Map<ChannelHandlerContext, WebSocketConnection> contextWsConnectionMap = new ConcurrentHashMap<>();



    public void createPlayerConnection(@NotNull ChannelHandlerContext context) {
        contextWsConnectionMap.put(
                context,
                new WebSocketConnection((SocketChannel) context.channel())
        );
    }

    public WebSocketConnection removePlayerConnection(@NotNull ChannelHandlerContext context) {
        return contextWsConnectionMap.remove(context);
    }

    @Nullable
    public WebSocketConnection getPlayerConnection(ChannelHandlerContext context) {
        return contextWsConnectionMap.get(context);
    }


}
