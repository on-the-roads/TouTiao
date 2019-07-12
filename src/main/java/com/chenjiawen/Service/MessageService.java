package com.chenjiawen.Service;

import com.chenjiawen.Dao.MessageDao;
import com.chenjiawen.Model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jiawen.chen on 2019/7/12.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    public int addMessage(Message message) {
        return messageDao.addMessage(message);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        //某用户下具体的消息，根据时间排序
        return messageDao.getConversationList(userId, offset, limit);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        //某对话下的具体未读消息列表
        return messageDao.getConversationDetail(conversationId, offset, limit);
    }

    public int getUnreadCount(int userId, String conversationId) {
        return messageDao.getConversationUnReadCount(userId, conversationId);
    }
}
