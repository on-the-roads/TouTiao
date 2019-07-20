package com.chenjiawen.async;

import java.util.List;

/**
 * 事件处理Handler接口
 * Created by jiawen.chen on 2019/7/20.
 */
public interface EventHandler {
    /**
     * 事件处理方法，不同EventHandler的实现类处理的方法不同
     * @param eventModel
     */
    void doHandler(EventModel eventModel);

    /**
     * 获取处理类支持的事件处理类型
     * @return
     */
    List<EventType> getSupportEventTypes();

}
