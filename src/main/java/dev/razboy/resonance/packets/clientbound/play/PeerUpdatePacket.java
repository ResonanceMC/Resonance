package dev.razboy.resonance.packets.clientbound.play;

import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.clientbound.ClientBoundPacket;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PeerUpdatePacket extends ClientBoundPacket {
    public static final String ONLINE = "online";
    public static final String POSITION = "position";
    public static final String WORLD = "dimension";
    @Override
    protected PacketType setPacketType() {
        return PacketType.PEER_UPDATE;
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
        return withIdActionBody(new JSONObject()
                .put("peers", new JSONArray(peers))).toString();
    }
    public boolean needsSending() {
        return peers.size() > 0;
    }
}
