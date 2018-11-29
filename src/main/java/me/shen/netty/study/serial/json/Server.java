package me.shen.netty.study.serial.json;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shenjianeng
 */
public class Server {

    @Slf4j
    public static class ServerChannelHandler extends SimpleChannelInboundHandler {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) {
            JsonMessageBean bean = (JsonMessageBean) msg;
            log.info("服务器收到: " + bean);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.error(cause.getMessage(), cause);
            ctx.close();
        }

    }


    private static final int POST = 9999;

    public static void main(String[] args) throws Exception {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new JsonEncoder());
                        ch.pipeline().addLast(new JsonDecoder<>(JsonMessageBean.class));
                        ch.pipeline().addLast(new ServerChannelHandler());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);


        ChannelFuture channelFuture = serverBootstrap.bind(POST).sync();
        channelFuture.channel().closeFuture().sync();

        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
