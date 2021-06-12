package net.thiccaxe.resonance.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import net.thiccaxe.resonance.network.packet.InboundPacket;
import net.thiccaxe.resonance.network.player.NettyWebSocketConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketProcessor {

    private final Map<ChannelHandlerContext, NettyWebSocketConnection> contextWsConnectionMap = new ConcurrentHashMap<>();

    public PacketProcessor() {

    }



    public void createPlayerConnection(@NotNull ChannelHandlerContext context) {
        contextWsConnectionMap.put(
                context,
                new NettyWebSocketConnection((SocketChannel) context.channel())
        );
    }

    public NettyWebSocketConnection removePlayerConnection(@NotNull ChannelHandlerContext context) {
        return contextWsConnectionMap.remove(context);
    }

    @Nullable
    public NettyWebSocketConnection getPlayerConnection(ChannelHandlerContext context) {
        return contextWsConnectionMap.get(context);
    }


    public void process(ChannelHandlerContext ctx, InboundPacket packet) {
        SocketChannel channel = (SocketChannel) ctx.channel();

        NettyWebSocketConnection webSocketConnection = contextWsConnectionMap.get(ctx);
        if (webSocketConnection == null) {
            // Unknown Error
            ctx.close();
            return;
        }

        if (!channel.isActive()) {
            return;
        }

        final ConnectionState connectionState = webSocketConnection.getConnectionState();

        System.out.println(connectionState.getDeclaringClass().getSimpleName() + "." + connectionState.name() + " / " + packet.getId());

    }
}
