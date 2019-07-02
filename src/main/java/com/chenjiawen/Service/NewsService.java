package com.chenjiawen.Service;

import com.chenjiawen.Dao.NewsDao;
import com.chenjiawen.Model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    public List<News> getUserIdAndOffset(int userId,int offset,int limit){
        List<News> newsList=newsDao.selectByUserIdAndOffset(userId,offset,limit);
        return newsList;
    }

}
