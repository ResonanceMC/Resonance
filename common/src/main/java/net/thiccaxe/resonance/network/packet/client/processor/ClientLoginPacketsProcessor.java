package net.thiccaxe.resonance.network.packet.client.processor;

import net.thiccaxe.resonance.network.packet.client.login.AuthTokenPacket;

public class ClientLoginPacketsProcessor extends ClientPacketProcessor {

    public ClientLoginPacketsProcessor() {
        register(0x00, AuthTokenPacket::new);
    }
}
