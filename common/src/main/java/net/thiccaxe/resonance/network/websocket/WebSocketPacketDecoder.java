package net.thiccaxe.resonance.network.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.thiccaxe.resonance.network.packet.InboundPacket;

import java.util.List;

public class WebSocketPacketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    private final Gson GSON = new Gson();

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame frame, List<Object> out) {
        try {
            final String text = frame.retain().text();

            JsonElement jsonElement = GSON.fromJson(text, JsonElement.class);

            if (jsonElement.isJsonObject()) {
                System.out.println("debug-01");
                out.add(getInboundPacket(jsonElement.getAsJsonObject()));
            } else if (jsonElement.isJsonArray()) {
                System.out.println("debug-02");
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    if (element.isJsonObject()) {
                        out.add(getInboundPacket(element.getAsJsonObject()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InboundPacket getInboundPacket(JsonObject jsonObject) {
        if (jsonObject.has("id") && jsonObject.has("body") &&
            jsonObject.get("id").getAsInt() >= 0 && jsonObject.get("body").isJsonObject()
        ) {
            return new InboundPacket(jsonObject.get("id").getAsInt(), jsonObject.getAsJsonObject("body"));
        }
        throw new IllegalArgumentException("Bad Packet");
    }
}
