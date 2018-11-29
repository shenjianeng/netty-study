package me.shen.netty.study.serial.json;

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
public class JsonMessageBean implements Serializable {

    private static final long serialVersionUID = -351999418228376702L;

    private String name;
    private int age;

}
