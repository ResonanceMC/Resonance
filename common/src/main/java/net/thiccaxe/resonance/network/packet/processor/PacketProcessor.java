package net.thiccaxe.resonance.network.packet.processor;

import net.thiccaxe.resonance.Resonance;
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

    private final Map<Object, WebSocketConnection> contextWsConnectionMap = new ConcurrentHashMap<>();

    private final ClientLoginPacketsProcessor loginPacketProcessor;

    private final @NotNull Resonance resonance;

    private final ConcurrentLinkedQueue<Packet> packetQueue = new ConcurrentLinkedQueue<>();

    private boolean running = false;


    public PacketProcessor(@NotNull Resonance resonance) {
        this.resonance = resonance;
        this.loginPacketProcessor = new ClientLoginPacketsProcessor();
    }

    public void createPlayerConnection() {
    }

    public WebSocketConnection removePlayerConnection() {
        return null;
    }

    @Nullable
    public WebSocketConnection getPlayerConnection() {
        return null;
    }


    public void process(InboundPacket inboundPacket) {

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
