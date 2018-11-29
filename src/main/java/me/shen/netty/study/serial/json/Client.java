package me.shen.netty.study.serial.json;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author shenjianeng
 */
public class Client {

    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new JsonDecoder<>(JsonMessageBean.class));
                        ch.pipeline().addLast(new JsonEncoder());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);


        ChannelFuture channelFuture = bootstrap.connect("localhost", SERVER_PORT).sync();

        for (int i = 0; i < 100; i++) {
            channelFuture.channel().write(new JsonMessageBean("JsonMessageBean" + i, i));
            if (i % 5 == 0) {
                channelFuture.channel().flush();
            }
        }
        channelFuture.channel().flush();

        channelFuture.channel().closeFuture().sync();

        eventLoopGroup.shutdownGracefully();
    }
}
