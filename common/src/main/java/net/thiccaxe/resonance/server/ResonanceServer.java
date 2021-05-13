package net.thiccaxe.resonance.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import net.thiccaxe.resonance.feature.FeatureEnableException;
import net.thiccaxe.resonance.feature.ResonanceFeature;
import net.thiccaxe.resonance.plugin.ResonancePlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class ResonanceServer implements ResonanceFeature {
    private final @NotNull String featureName = "ResonanceServer";
    private final @NotNull @Unmodifiable List<String> featureDescription = List.of(
            "Webserver for Resonance,",
            "that handles HTTP / WS requests" // todo https/wss :(
    );

    private @NotNull
    final ResonancePlugin plugin;

    private final EventLoopGroup workerGroup;
    private final EventLoopGroup bossGroup;

    private final int port;
    private volatile boolean enabled = false;

    public ResonanceServer(@NotNull ResonancePlugin plugin, final int port) {
        this.plugin = plugin;
        workerGroup = new NioEventLoopGroup(1);
        bossGroup = new NioEventLoopGroup(1);
        this.port = port;
    }


    @Override
    public void enable() throws FeatureEnableException {
        plugin.logger().info("Starting " + name() + " on Port: " + port);
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
                disable();
            }
        });
        enabled = true;
    }


    @Override
    public void disable() {
        plugin.logger().info("Stopping Resonance Server ...");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        enabled = false;
    }


    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public @NotNull String name() {
        return featureName;
    }

    @Override
    public @NotNull @Unmodifiable List<String> description() {
        return featureDescription;
    }
}
