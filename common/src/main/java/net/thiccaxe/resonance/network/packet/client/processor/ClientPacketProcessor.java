package net.thiccaxe.resonance.network.packet.client.processor;

import net.thiccaxe.resonance.network.packet.client.ClientPacket;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class ClientPacketProcessor {

    private static final int MAX_PACKET_ID = 0x10;

    private final PacketSupplier[] packetSuppliers = new PacketSupplier[MAX_PACKET_ID];

    public void register(int id, @NotNull PacketSupplier packetSupplier) {
        this.packetSuppliers[id] = packetSupplier;
    }

    public ClientPacket getPacket(int id) {
        if (id > MAX_PACKET_ID) {
            throw new IllegalArgumentException("ID 0x" + Integer.toHexString(id) + " is not valid!");
        }

        PacketSupplier supplier = packetSuppliers[id];
        if (supplier == null) {
            throw new IllegalArgumentException("ID 0x" + Integer.toHexString(id) + " is not registered!");
        }
        return supplier.get();
    }



    public interface PacketSupplier extends Supplier<ClientPacket> {}
}
