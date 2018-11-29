package me.shen.netty.study.serial.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author shenjianeng
 */
public class KryoDecoder extends LengthFieldBasedFrameDecoder {

    public KryoDecoder() {
        super(1024 * 1024,
                0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        Input input = new Input(new ByteBufInputStream(frame));
        Kryo kryo = new Kryo();
        return kryo.readClassAndObject(input);
    }
}
