package dev.razboy.resonance.network;

import dev.razboy.resonance.packets.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class Request {
    public final Connection connection;
    public FullHttpRequest fullHttpRequest;
    public TextWebSocketFrame webSocketFrame;
    public Packet packet;
    public final ChannelHandlerContext ctx;

    public Request(Connection connection, ChannelHandlerContext ctx, TextWebSocketFrame webSocketFrame) {
        this.connection = connection;
        this.ctx = ctx;
        this.webSocketFrame = webSocketFrame;
        this.fullHttpRequest = null;
        this.packet = Packet.readPacket(webSocketFrame.retain().text());
    }
    public Request(Connection connection, ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        this.connection = connection;
        this.ctx = ctx;
        this.webSocketFrame = null;
        this.fullHttpRequest = fullHttpRequest;
    }
    public Request(Connection connection, Packet packet) {
        this.connection = connection;
        this.ctx = connection.getCtx();
        this.packet = packet;
    }
    public Request(Request request) {
        connection = request.connection;
        ctx = request.ctx;
        packet = request.packet;
        fullHttpRequest = request.fullHttpRequest;
        webSocketFrame = request.webSocketFrame;
    }
    public Request setFullHttpRequest(FullHttpRequest r) {
        fullHttpRequest = r;
        return this;
    }
    public Request setPacket(Packet p) {
        packet = p;
        return this;
    }

}
