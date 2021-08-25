package net.thiccaxe.resonance.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketRequestHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    public WebSocketRequestHandler() {
        super();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame textWebSocketFrame) {
            final String message = textWebSocketFrame.retain().text();
            System.out.println(message);
            ctx.write(msg);
        }
    }
}
