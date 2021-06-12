package net.thiccaxe.resonance.network.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class WebSocketPacketDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame frame, List<Object> out) {
        try {
            System.out.println(frame.retain().text());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
