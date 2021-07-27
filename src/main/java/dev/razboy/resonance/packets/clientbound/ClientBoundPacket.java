package dev.razboy.resonance.packets.clientbound;

import dev.razboy.resonance.packets.Packet;

public abstract class ClientBoundPacket extends Packet {
    public boolean isServerBound() {
        return false;
    }
    public boolean isClientBound() {
        return true;
    }
}
