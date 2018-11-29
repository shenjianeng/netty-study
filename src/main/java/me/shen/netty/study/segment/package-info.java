/**
 * TCP粘包/拆包的解决
 * 1.设定消息边界
 * 2.设定消息长度
 * 3.head + body ,head中设置body的大小
 */
package me.shen.netty.study.segment;