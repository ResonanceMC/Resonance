/*
 * This file is part of Resonance, licensed under the MIT License.
 *
 * Copyright (c) 2021 thiccaxe
 * Copyright (c) 2021 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.thiccaxe.resonance.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import net.thiccaxe.resonance.feature.Feature;
import net.thiccaxe.resonance.feature.FeatureEnableException;
import net.thiccaxe.resonance.logging.ResonanceLogger;
import net.thiccaxe.resonance.plugin.ResonancePlugin;
import org.jetbrains.annotations.NotNull;

public class ResonanceServer implements Feature {

    private @NotNull
    final ResonancePlugin plugin;

    private final EventLoopGroup workerGroup;
    private final EventLoopGroup bossGroup;

    private int port;
    private volatile boolean enabled = false;

    public ResonanceServer(@NotNull ResonancePlugin plugin, int port) {
        this.plugin = plugin;
        workerGroup = new NioEventLoopGroup(1);
        bossGroup = new NioEventLoopGroup(1);
        this.port = port;
    }

    @Override
    public void enable() {
        logger().info("Starting on Port: " + port);
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
                                                logger().info(ctx.channel().remoteAddress().toString() + " | " + msg.uri());
                                                FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                                                        HttpResponseStatus.OK,
                                                        Unpooled.copiedBuffer("Hello, World".getBytes()));
                                                res.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
                                                if (HttpUtil.isKeepAlive(msg)) {
                                                    res.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                                                }
                                                ChannelFuture future = ctx.channel().writeAndFlush(res);
                                                if (!HttpUtil.isKeepAlive(msg)) {
                                                    future.addListener(ChannelFutureListener.CLOSE);
                                                }
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
        logger().info("Stopping Resonance Server ...");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        enabled = false;
    }


    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public @NotNull ResonanceLogger logger() {
        return plugin.logger();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

}
