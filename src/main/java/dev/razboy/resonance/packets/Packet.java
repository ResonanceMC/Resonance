package dev.razboy.resonance.packets;

import dev.razboy.resonance.packets.serverbound.ServerBoundPacket;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class Packet {
    protected PacketType packetType = PacketType.INVALID;
    protected Integer messageId;

    protected abstract PacketType setPacketType();

    public Packet() {
        packetType = setPacketType();
    }

    public abstract String read();

    public int getId() {return packetType.id;}
    boolean isServerBound() {return false;}
    boolean isClientBound() {return false;}

    public static Packet readPacket(String message) throws MalformedPacketException {
        try {
            JSONObject json = new JSONObject(message);
            if (json.has("action")) {
                Packet packet = PacketType.getPacket(json.get("action").toString());
                if (packet != null) {
                    if (packet instanceof ServerBoundPacket) {
                        packet.messageId = readId(json);
                        ((ServerBoundPacket) packet).readJson(json);
                    }

                }
                return packet;

            }
        } catch (MalformedPacketException e) {
            throw new MalformedPacketException("Error Parsing Packet");
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static Integer readId(JSONObject json) {
        try {
            if (json.has("id")) {
                return json.getInt("id");
            }
        } catch (Exception ignored){}
        return null;
    }
    protected JSONObject withId() {
        JSONObject json = new JSONObject();
        if (messageId != null) {
            json.put("id", messageId.intValue());
        }

        return json;
    }
    protected JSONObject withIdAction() {
        return withId().put("action", packetType.action);
    }

    protected JSONObject withIdActionBody(JSONObject body) {
        return withIdAction().put("body", body);
    }

    public Packet setMessageId(Integer id) {
        messageId = id;
        return this;
    }
    public Integer getMessageId() {
        return messageId;
    }

    public String repr() {
        return getClass().getSimpleName() + "()";
    }
}
