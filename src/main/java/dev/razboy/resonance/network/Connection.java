package dev.razboy.resonance.network;

import dev.razboy.resonance.Resonance;
import dev.razboy.resonance.util.HttpTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class Connection extends SimpleChannelInboundHandler<Object> {
    private ChannelHandlerContext context;
    private int requests = 0;
    private String remote = "";
    private boolean websocketConnection = false;
    private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'r', 'o', 'l', 'd' };
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) {
        context = ctx;
        requests++;


        if (remote.isEmpty() && obj instanceof FullHttpRequest) {
            remote = HttpTools.getRemoteAddress(ctx, (FullHttpRequest) obj);
        }

        if (obj instanceof TextWebSocketFrame) {
            TextWebSocketFrame frame = (TextWebSocketFrame) obj;
            String text = frame.retain().text();
            Resonance.getWebSocketRequestManager().addIncoming(new Request(this, ctx, frame));
        } else if (obj instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) obj;
            String uri = request.uri();
            System.out.println("(" + requests + ")/" + remote + ": " + request.uri());
            //System.out.println("(" + requests + ")/" + remote + ": " + (uri.length()>50?uri.substring(0,49):uri));
            if (!HttpTools.upgrade(ctx, request)) {
                Resonance.getHttpRequestManager().addIncoming(new Request(this, ctx, request));
                HttpTools.writeTemplateResponse(ctx, remote, request);
            } else {
                websocketConnection = true;
            }
        }
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (websocketConnection) {
            Resonance.getWebSocketRequestManager().addIncoming(new Request(this, ctx, new TextWebSocketFrame("{\"action\":\"user_disconnect\"}")));
        }
    }

    public String getRemote() {return remote;}
    public int getRequests() {return requests;}
    public ChannelHandlerContext getCtx() {return context;}
}
