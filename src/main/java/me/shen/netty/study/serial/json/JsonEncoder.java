package me.shen.netty.study.serial.json;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * @author shenjianeng
 */
public class JsonEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(out);
        JSON.writeJSONString(byteBufOutputStream, msg);
        byteBufOutputStream.write(System.getProperty("line.separator").getBytes(StandardCharsets.UTF_8));
    }
}

