package dev.razboy.resonance.packets.clientbound.play;

import dev.razboy.resonance.packets.PacketType;
import dev.razboy.resonance.packets.clientbound.ClientBoundPacket;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserUpdatePacket extends ClientBoundPacket {
    public static final String ONLINE = "online";
    public static final String POSITION = "position";
    public static final String WORLD = "dimension";

    @Override
    protected PacketType setPacketType() {
        return PacketType.USER_UPDATE;
    }

    private JSONObject user;
    private String type;

    public void setUser(JSONObject user) {
        this.user = user;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String repr() {
        return getClass().getSimpleName() + "(user=" + user.toString() + ")";
    }

    @Override
    public String read() {
        user.remove("data");
        return withIdActionBody(
                user.put("type", type)
        ).toString();

    }
}
