package com.chenjiawen.async;

/**
 * 事件类型枚举
 * Created by jiawen.chen on 2019/7/20.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3),
    SCORE(4);


    private int value;
    EventType(int value)
    {
        this.value=value;
    }
}
