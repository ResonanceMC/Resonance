package net.thiccaxe.resonance.network.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.thiccaxe.resonance.network.PacketProcessor;
import net.thiccaxe.resonance.network.packet.InboundPacket;
import net.thiccaxe.resonance.network.player.NettyWebSocketConnection;
import org.jetbrains.annotations.NotNull;

public class WebSocketChannel extends SimpleChannelInboundHandler<InboundPacket> {
    private final PacketProcessor packetProcessor;

    public WebSocketChannel(@NotNull PacketProcessor packetProcessor) {
        this.packetProcessor = packetProcessor;
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        packetProcessor.createPlayerConnection(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InboundPacket packet) throws Exception {
        try {
            System.out.println(packet.getId() + ", " + packet.getBody());
            packetProcessor.process(ctx, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyWebSocketConnection connection = packetProcessor.removePlayerConnection(ctx);
        if (connection != null) {
            //stuff
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!ctx.channel().isActive()) {
            return;
        }

        ctx.close();
    }
}
