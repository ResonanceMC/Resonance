package net.thiccaxe.resonance.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import net.thiccaxe.resonance.plugin.ResonancePlugin;
import org.jetbrains.annotations.NotNull;

public class ResonanceServer {
    private @NotNull ResonancePlugin plugin;

    private EventLoopGroup workerGroup;
    private EventLoopGroup bossGroup;

    private volatile boolean running = false;

    public ResonanceServer(@NotNull ResonancePlugin plugin) {
        this.plugin = plugin;
        workerGroup = new NioEventLoopGroup(1);
        bossGroup = new NioEventLoopGroup(1);
    }



    public void start(final int port) {
        running = true;
        plugin.logger().info("Starting Resonance Server on Port: " + port);
        plugin.scheduler().async().execute(() -> {
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<>() {
                            @Override
                            protected void initChannel(Channel ch) {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new HttpServerCodec())
                                        .addLast(new HttpObjectAggregator(1048576)) // 1024^2 ?
                                        .addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                            @Override
                                            protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
                                                FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                                        HttpResponseStatus.TEMPORARY_REDIRECT,
                                                        Unpooled.copiedBuffer("e".getBytes()));
                                                res.headers().set(HttpHeaderNames.LOCATION, "https://pleaserickroll.me");
                                                ctx.channel().writeAndFlush(res);
                                            }
                                        });
                            }
                        });
                bootstrap.bind(port).channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stop();
            }
        });


    }

    public void stop() {
        plugin.logger().info("Stopping Resonance Server ...");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public boolean isRunning() {
        return running;
    }

}
