package com.chenjiawen.async.handler;

import com.chenjiawen.Model.Message;
import com.chenjiawen.Model.User;
import com.chenjiawen.Service.MessageService;
import com.chenjiawen.Service.UserService;
import com.chenjiawen.async.EventHandler;
import com.chenjiawen.async.EventModel;
import com.chenjiawen.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 点赞事件的处理Handler
 * Created by jiawen.chen on 2019/7/20.
 */
@Component  //漏了这里导致EventConsumer的afterPropertiesSet中getBeansOfType方法没扫描到
public class LikeHandler implements EventHandler {
    /**
     * 当点赞事件发生时,实体的拥有者就会收到短信通知，自己发布的实体被被人点赞
     * @param eventModel
     */
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LikeHandler.class);

    @Override
    public void doHandler(EventModel eventModel) {
        Message message=new Message();
        message.setFromId(eventModel.getActorId());
//        message.setToId(eventModel.getTargetId());
        //这里为了显示效果，当前用户给被人点赞后自己收到一个消息通知
        message.setToId(eventModel.getActorId());
        message.setCreatedDate(new Date());
        User ActUser=userService.getUserBYid(message.getFromId());
        message.setContent("用户" + ActUser.getName() + "赞了你的资讯"
                + ",http://127.0.0.1:8088/news/" + eventModel.getEntityId());
        message.setHasRead(0);
        int fromId=message.getFromId();
        int toId=message.getToId();
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);
        System.out.println("Liked");
        LOGGER.info("处理赞踩事件，发送消息通知");
        System.out.println("处理赞踩事件，发送消息通知");
    }


    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
