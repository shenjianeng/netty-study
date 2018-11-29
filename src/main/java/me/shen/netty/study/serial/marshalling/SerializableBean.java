package me.shen.netty.study.serial.marshalling;

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
public class SerializableBean implements Serializable {
    private static final long serialVersionUID = -8051194378758265501L;

    private String name;
    private int age;
}
