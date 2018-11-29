package me.shen.netty.study.segment;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author shenjianeng
 */
public class FixedLengthClient {

    @Slf4j
    @ChannelHandler.Sharable
    public static class FixedLengthClientChannelHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            //服务端只能收到12345, abc因为长度不足5个收不到
            //ctx.writeAndFlush(Unpooled.copiedBuffer("12345abc", StandardCharsets.UTF_8));
            ctx.writeAndFlush(Unpooled.copiedBuffer("12345abc  ", StandardCharsets.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.error(cause.getMessage(), cause);
            ctx.close();
        }

    }


    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) throws Exception {
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new FixedLengthClientChannelHandler());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture channelFuture = bootstrap.connect("localhost", SERVER_PORT).sync();
        channelFuture.channel().closeFuture().sync();

        loopGroup.shutdownGracefully();


    }
}
