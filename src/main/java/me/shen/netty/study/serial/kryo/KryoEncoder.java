package me.shen.netty.study.serial.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author shenjianeng
 */
@Slf4j
public class KryoEncoder extends MessageToByteEncoder<Object> {

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        final Kryo kryo = new Kryo();

        int startIdx = out.writerIndex();

        try (final ByteBufOutputStream byteOutputStream = new ByteBufOutputStream(out);
             final Output output = new Output(byteOutputStream)) {

            //头部先占4个字节
            byteOutputStream.write(LENGTH_PLACEHOLDER);

            kryo.writeClassAndObject(output, msg);

            output.flush();

            int endIdx = out.writerIndex();

            //在头部预留的4个字节内写入长度信息
            out.setInt(startIdx, endIdx - startIdx - 4);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
