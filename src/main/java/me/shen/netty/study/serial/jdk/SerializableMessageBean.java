package me.shen.netty.study.serial.jdk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shenjianeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerializableMessageBean implements Serializable {
    private String name;
    private int age;
}
