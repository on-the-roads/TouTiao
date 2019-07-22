package com.chenjiawen.async.handler;

import com.chenjiawen.Model.Message;
import com.chenjiawen.Model.User;
import com.chenjiawen.Service.MessageService;
import com.chenjiawen.Service.UserService;
import com.chenjiawen.Util.MailSender;
import com.chenjiawen.async.EventHandler;
import com.chenjiawen.async.EventModel;
import com.chenjiawen.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by jiawen.chen on 2019/7/22.
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;
    @Autowired
    MailSender mailSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(LikeHandler.class);

    @Override
    public void doHandler(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(eventModel.getActorId());
        message.setToId(eventModel.getActorId());
        message.setCreatedDate(new Date());
        User ActUser = userService.getUserBYid(message.getFromId());
        message.setContent("您上次登录的ip异常");
        message.setHasRead(0);
        int fromId = message.getFromId();
        int toId = message.getToId();
        message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));
        messageService.addMessage(message);


        //发送登录异常邮件通知
        Map<String,Object> modelmap=new HashMap<String,Object>();
        modelmap.put("username",eventModel.getExts().get("userName"));
        mailSender.sendWithHTMLTemplate(eventModel.getExts().get("email"),"登录异常","mails/welcome.html",modelmap);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
