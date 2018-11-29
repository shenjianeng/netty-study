package me.shen.netty.study.serial.marshalling;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.serial.SerialMarshallerFactory;


/**
 * @author shenjianeng
 */
public abstract class MarshallingUtil {

    public static MarshallingDecoder buildMarshallingDecoder() {
        final UnmarshallerProvider provider = new DefaultUnmarshallerProvider(new SerialMarshallerFactory(), new MarshallingConfiguration());
        return new MarshallingDecoder(provider, 1024 * 1024);
    }

    public static MarshallingEncoder buildMarshallingEncoder() {
        final MarshallerProvider provider = new DefaultMarshallerProvider(new SerialMarshallerFactory(), new MarshallingConfiguration());
        return new MarshallingEncoder(provider);
    }
}
