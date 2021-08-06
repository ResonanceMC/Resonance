package net.thiccaxe.resonance.network.javalin.codec;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.thiccaxe.resonance.network.packet.InboundPacket;
import net.thiccaxe.resonance.network.packet.processor.PacketProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WebSocketPacketDecoder {

    private final Gson GSON = new Gson();

    protected List<InboundPacket> decode(String text) {
        try {
            JsonElement jsonElement = GSON.fromJson(text, JsonElement.class);
            List<InboundPacket> out = new ArrayList<>();
            if (jsonElement.isJsonObject()) {
                System.out.println(jsonElement.getAsJsonObject().toString());
                out.add(getInboundPacket(jsonElement.getAsJsonObject()));
            }
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private InboundPacket getInboundPacket(JsonObject jsonObject) {
        if (jsonObject.has("id") && jsonObject.has("body") &&
            jsonObject.getAsJsonPrimitive("id").isNumber() && jsonObject.get("body").isJsonObject()
        ) {
            return new InboundPacket(
                    jsonObject.getAsJsonPrimitive("id").getAsNumber().intValue(),
                    jsonObject.getAsJsonObject("body"),
                    optionalField(jsonObject, "messageId"),
                    optionalField(jsonObject, "bearer")
            );
        }
        throw new IllegalArgumentException("Bad Packet");
    }

    private Optional<String> optionalField(JsonObject jsonObject, String field) {
        String value = null;
        if (jsonObject.has(field) && jsonObject.getAsJsonPrimitive(field).isString()) {
            value = jsonObject.getAsJsonPrimitive(field).getAsString();
        }
        return Optional.ofNullable(value);
    }
}
