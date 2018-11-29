package me.shen.netty.study.serial.json;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * @author shenjianeng
 */
public class JsonDecoder<T> extends LineBasedFrameDecoder {

    private final Class<T> clazz;

    public JsonDecoder(Class<T> clazz) {
        super(1024 * 1024);
        this.clazz = clazz;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
        if (frame == null) {
            return null;
        }

        ByteBufInputStream byteBufInputStream = new ByteBufInputStream(frame);
        return JSON.parseObject(byteBufInputStream.readLine(), clazz);
    }
}
