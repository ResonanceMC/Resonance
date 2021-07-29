package net.thiccaxe.resonance.network.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String WEBSOCKET_URI;


    private static final byte[] CONTENT = "<h1>Remsonancme</h1>".getBytes(StandardCharsets.UTF_8);


    public HttpRequestHandler(String WEBSOCKET_URI) {
        super();
        this.WEBSOCKET_URI = WEBSOCKET_URI;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (WEBSOCKET_URI.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            FullHttpResponse response = new DefaultFullHttpResponse(
                    request.protocolVersion(),
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(CONTENT)
            );
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
            final boolean isKeepAlive = HttpUtil.isKeepAlive(request);

            if (isKeepAlive) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, CONTENT.length);
            }
            ctx.write(response);
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!isKeepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }

        }
    }

    private void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.CONTINUE
        );
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
