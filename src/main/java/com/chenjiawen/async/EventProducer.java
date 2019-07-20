package com.chenjiawen.async;

import com.alibaba.fastjson.JSON;
import com.chenjiawen.Util.JedisAdapter;
import com.chenjiawen.Util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 事件产生者
 * Created by jiawen.chen on 2019/7/20.
 */
@Service
public class EventProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * Redis列表用过消息队列，将事件序列化后存放在Redis列表中
     * @param eventModel
     * @return
     */
    public boolean MakeEvent(EventModel eventModel) {
        try {
            //序列化为JSON格式
            String jsonObject = JSON.toJSONString(eventModel);
            jedisAdapter.lpush(RedisKeyUtil.getEventQueue(), jsonObject);
            return true;
        } catch (Exception e) {
            LOGGER.error("向消息队列中添加事件失败");
            return false;
        }
    }
}
