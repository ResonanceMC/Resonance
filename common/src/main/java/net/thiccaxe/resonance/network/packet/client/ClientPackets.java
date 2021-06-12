package net.thiccaxe.resonance.network.packet.client;

public enum ClientPackets {
    TEST(0x00);

    private final int id;

    ClientPackets(int id) {
        this.id = id;
    }

}
