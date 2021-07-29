package net.thiccaxe.resonance.network.netty.codec;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.thiccaxe.resonance.network.packet.InboundPacket;

import java.util.List;
import java.util.Optional;

public class WebSocketPacketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    private final Gson GSON = new Gson();

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame frame, List<Object> out) {
        try {
            final String text = frame.retain().text();

            JsonElement jsonElement = GSON.fromJson(text, JsonElement.class);

            if (jsonElement.isJsonObject()) {
                System.out.println(jsonElement.getAsJsonObject().toString());
                out.add(getInboundPacket(jsonElement.getAsJsonObject()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
