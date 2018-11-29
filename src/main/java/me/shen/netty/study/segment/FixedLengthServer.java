package me.shen.netty.study.segment;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shenjianeng
 */
public class FixedLengthServer {

    @Slf4j
    @ChannelHandler.Sharable
    public static class FixedLengthServerChannelHandler extends SimpleChannelInboundHandler {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) {
            String str = (String) msg;
            log.info("服务器收到: " + str);
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
                        //固定消息长度
                        ch.pipeline().addLast(new FixedLengthFrameDecoder(5));
                        //字符串数据解码器,阅读源码可知:out.add(msg.toString(charset));
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new FixedLengthServerChannelHandler());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);


        ChannelFuture channelFuture = serverBootstrap.bind(POST).sync();
        channelFuture.channel().closeFuture().sync();

        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

    }
}
