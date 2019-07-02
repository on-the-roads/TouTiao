package com.nowcoder;

import com.chenjiawen.Dao.NewsDao;
import com.chenjiawen.Dao.UserDao;
import com.chenjiawen.Model.News;
import com.chenjiawen.Model.User;
import com.ToutiaoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class DaoTests {

	@Autowired
	private UserDao userDao;

	@Autowired
	private NewsDao newsDao;

	@Test
	public void initData() {
		Random random=new Random();
		for (int i = 0; i < 10; i++) {
			User user=new User();
			user.setName(String.format("User%d",i));
			user.setPassword(String.valueOf(random.nextInt(1231)));
			user.setSalt("");
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			userDao.addUser(user);
			user.setPassword("passwo");
			userDao.updatePassById(user);

			//=====================newsDao=======================
            News news=new News();
            news.setCommentCount(i*124);
            Date date=new Date();
            date.setTime(date.getTime()+i*1000*3600*24);
            news.setCreatedDate(date);
            news.setId(i);
            news.setLikeCount(i);
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            news.setTitle(String.format("TITLE%d",i));
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png",random.nextInt(1000)));
            news.setUserId(i+1);
            newsDao.addNews(news);
		}
        List<News> newsLIst=newsDao.selectByUserIdAndOffset(3,0,20);
//		userDao.deleteById(1);
//		Assert.assertNotNull(userDao.selectById(1));

	}

}
