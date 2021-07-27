package dev.razboy.resonance.packets.serverbound;

import dev.razboy.resonance.packets.MalformedPacketException;
import dev.razboy.resonance.packets.Packet;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ServerBoundPacket extends Packet {
    public boolean isServerBound() {
        return true;
    }
    public boolean isClientBound() {
        return false;
    }

    @Override
    public String read() {return null;}

    public abstract void readJson(JSONObject json) throws MalformedPacketException;


}
