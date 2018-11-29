package me.shen.netty.study.serial.marshalling;

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
                        ch.pipeline().addLast(MarshallingUtil.buildMarshallingDecoder());
                        ch.pipeline().addLast(MarshallingUtil.buildMarshallingEncoder());
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);


        ChannelFuture channelFuture = bootstrap.connect("localhost", SERVER_PORT).sync();

        channelFuture.channel().writeAndFlush(new SerializableBean("哈哈", 7777));

        channelFuture.channel().closeFuture().sync();

        eventLoopGroup.shutdownGracefully();
    }
}
