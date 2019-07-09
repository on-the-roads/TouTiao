package com.chenjiawen.Service;

import com.chenjiawen.Dao.CommentDao;
import com.chenjiawen.Model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    public int addComment(Comment comment)
    {
        return commentDao.addComment(comment);
    }

    public List<Comment> selectByEntity(int entityId,int entityType)
    {
        return  commentDao.selectByEntity(entityId,entityType);
    }
    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }

}
