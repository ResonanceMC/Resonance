package net.thiccaxe.resonance.network.packet.processor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import net.thiccaxe.resonance.Resonance;
import net.thiccaxe.resonance.network.ConnectionState;
import net.thiccaxe.resonance.network.packet.InboundPacket;
import net.thiccaxe.resonance.network.packet.client.ClientPacket;
import net.thiccaxe.resonance.network.packet.client.processor.ClientLoginPacketsProcessor;
import net.thiccaxe.resonance.network.user.WebSocketConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PacketProcessor implements Runnable {

    private final Map<ChannelHandlerContext, WebSocketConnection> contextWsConnectionMap = new ConcurrentHashMap<>();

    private final ClientLoginPacketsProcessor loginPacketProcessor;

    private final @NotNull Resonance resonance;

    private final ConcurrentLinkedQueue<Packet> packetQueue = new ConcurrentLinkedQueue<>();

    private boolean running = false;


    public PacketProcessor(@NotNull Resonance resonance) {
        this.resonance = resonance;
        this.loginPacketProcessor = new ClientLoginPacketsProcessor();
    }

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


    public void process(ChannelHandlerContext ctx, InboundPacket inboundPacket) {
        SocketChannel channel = (SocketChannel) ctx.channel();

        WebSocketConnection userConnection = contextWsConnectionMap.get(ctx);
        if (userConnection == null) {
            //createPlayerConnection(ctx);
            //userConnection = contextWsConnectionMap.get(ctx);
            return;
        }

        if (!channel.isActive()) {
            return;
        }
        final ConnectionState connectionState = userConnection.getConnectionState();
        System.out.println(connectionState.getDeclaringClass().getSimpleName() + "." + connectionState.name() + " / " + inboundPacket.getId());

        ClientPacket packet;
        switch (connectionState) {
            case LOGIN: packet = loginPacketProcessor.getPacket(inboundPacket.getId()); break;
            default: packet = null; break;
        }
        if (packet != null) {
            packet.read(inboundPacket);
            queuePacket(userConnection, packet);
        }


    }

    private void queuePacket(WebSocketConnection userConnection, ClientPacket packet) {
        packetQueue.add(new Packet(userConnection, packet));
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        if (running && !packetQueue.isEmpty()) {
            while (!packetQueue.isEmpty()) {
                Packet packet = packetQueue.poll();
                if (packet == null) break;
                packet.process();
            }
        }
    }


    public static class Packet {
        private final @NotNull WebSocketConnection userConnection;
        private final @NotNull ClientPacket packet;

        public Packet(@NotNull WebSocketConnection userConnection, @NotNull ClientPacket packet) {
            this.userConnection = userConnection;
            this.packet = packet;
        }

        public WebSocketConnection getUserConnection() {
            return userConnection;
        }

        public ClientPacket getPacket() {
            return packet;
        }

        public void process() {
            packet.process(userConnection);
        }
    }
}
