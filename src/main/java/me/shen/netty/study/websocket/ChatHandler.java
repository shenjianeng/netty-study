package me.shen.netty.study.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author shenjianeng
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 用于记录所有的在线客户端
     */
    private static final ChannelGroup CLIENT_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        final String msgText = msg.text();
        log.info("服务器接收到消息: " + msgText);
        if (msgText.startsWith("all-")) {
            publishForAll(LocalDateTime.now() + ", 群发消息: " + msgText);
        } else {
            publishForCurrent(ctx, LocalDateTime.now() + ", 单发消息: " + msgText);
        }

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress() + "加入聊天室");
        CLIENT_GROUP.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress() + "离开聊天室");
        CLIENT_GROUP.remove(ctx.channel());
    }

    /**
     * 将消息发送给当前客户端
     */
    private void publishForCurrent(ChannelHandlerContext ctx, String text) {
        ctx.writeAndFlush(new TextWebSocketFrame(text));
    }

    /**
     * 将消息发送给所有的在线客户端
     */
    private void publishForAll(String text) {
        CLIENT_GROUP.writeAndFlush(new TextWebSocketFrame(text));
    }
}
