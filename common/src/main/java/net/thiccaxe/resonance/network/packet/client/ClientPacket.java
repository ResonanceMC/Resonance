package net.thiccaxe.resonance.network.packet.client;

import net.thiccaxe.resonance.network.packet.InboundPacket;
import net.thiccaxe.resonance.network.user.WebSocketConnection;
import org.jetbrains.annotations.NotNull;

public interface ClientPacket {

    default void read(@NotNull InboundPacket packet) {
        throw new UnsupportedOperationException("Unknown Packet");
    }

    void process(@NotNull WebSocketConnection userConnection);

}
