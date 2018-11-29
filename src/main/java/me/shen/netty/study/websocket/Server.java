package me.shen.netty.study.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shenjianeng
 */
@Slf4j
public class Server {

    private static final int POST = 9999;

    public static void main(String[] args) throws Exception {

        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //websocket是基于http,先对http进行设置
                        ch.pipeline().addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(1023 * 32))
                                .addLast(new ChunkedWriteHandler());
                        //对websocket 进行设置,指定客户端访问路径
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
                        //自定义
                        ch.pipeline().addLast(new ChatHandler());
                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(POST).sync();

        log.info("服务启动...");

        channelFuture.channel().closeFuture().sync();

        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
