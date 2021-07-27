package dev.razboy.resonance.packets.clientbound.play;

import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.clientbound.ClientBoundPacket;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OPeerInfoPacket extends ClientBoundPacket {
    @Override
    protected PacketType setPacketType() {
        return PacketType.PEER_INFO;
    }

    private final ArrayList<JSONObject> peers = new ArrayList<>();

    public void addPeer(JSONObject peer) {
        peers.add(peer);
    }

    @Override
    public String repr() {
        return getClass().getSimpleName() + "(peers=" + peers.toString() + ")";
    }

    @Override
    public String read() {
        return withIdActionBody(new JSONObject().put("peers", new JSONArray(peers))).toString();
    }
}
