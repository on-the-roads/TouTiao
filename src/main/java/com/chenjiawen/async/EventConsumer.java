package com.chenjiawen.async;

import com.alibaba.fastjson.JSONObject;
import com.chenjiawen.Util.JedisAdapter;
import com.chenjiawen.Util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件消费者
 * Created by  jiawen.chen on 2019/7/20.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

    //由上下文映射所有实现EventHandler的实现类
    private ApplicationContext applicationContext;
    //需要一个map来存放一个具体事件类型能被哪些Handler所处理,用于后续对每个事件的处理
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 初始化完成后用线程一直监听队列等待处理事件
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {

        LOGGER.info("初始化完成后用线程一直监听队列等待处理事件");
        //利用上下文获取所有实现EventHandler的实现类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                //对每一个EventHandler的实现类，都获取它们能够处理的事件类型
                List<EventType> supportEventTypes = entry.getValue().getSupportEventTypes();
                //对每一个事件类型，将其作为key，value为能够处理该事件类型的Handler列表，形成时间类型和处理Handler的映射关系
                for (EventType eventType : supportEventTypes) {
                    if (!config.containsKey(eventType))
                        config.put(eventType, new ArrayList<EventHandler>());
                    config.get(eventType).add(entry.getValue());
                }
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String eventQueueName = RedisKeyUtil.getEventQueue();
                    List<String> eventJsons = jedisAdapter.brpop(eventQueueName);
                    for (String eventJson : eventJsons) {
                        //队列阻塞弹出先弹出队列名，所以要做下过滤
                        if (eventJson.equals(eventQueueName))
                            continue;
                        EventModel eventModel = JSONObject.parseObject(eventJson, EventModel.class);
                        if (!config.containsKey(eventModel.getEventType())) {
                            LOGGER.error("不能识别的事件类型{}", eventJson);
                            continue;
                        }
                        //由前面的map获得一个具体事件的各种处理Handler
                        List<EventHandler> eventHandlers = config.get(eventModel.getEventType());
                        for (EventHandler eventHandler : eventHandlers) {
                            eventHandler.doHandler(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();

//        Monitor();
    }

//    private void Monitor() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    String eventQueueName = RedisKeyUtil.getEventQueue();
//                    List<String> eventJsons = jedisAdapter.brpop(eventQueueName);
//                    for (String eventJson : eventJsons) {
//                        //队列阻塞弹出先弹出队列名，所以要做下过滤
//                        if (eventJson.equals(eventQueueName))
//                            continue;
//                        EventModel eventModel = JSONObject.parseObject(eventJson, EventModel.class);
//                        if (!config.containsKey(eventModel.getEventType())) {
//                            LOGGER.error("不能识别的事件类型{}", eventJson);
//                            continue;
//                        }
//                        //由前面的map获得一个具体事件的各种处理Handler
//                        List<EventHandler> eventHandlers = config.get(eventModel.getEventType());
//                        for (EventHandler eventHandler : eventHandlers) {
//                            eventHandler.doHandler(eventModel);
//                        }
//                    }
//                }
//            }
//        });
//        thread.start();
//    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
