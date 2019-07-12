package com.nowcoder;

import com.chenjiawen.Dao.*;
import com.chenjiawen.Model.*;
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

	@Autowired
	private LoginticketDao loginticketDao;
	@Autowired
	private CommentDao commentDao;

	@Autowired
	private MessageDao messageDao;

	@Test
	public void initData() {
		Random random=new Random();
		for (int i = 0; i < 10; i++) {
			User user=new User();
			user.setName(String.format("cjw%d",i));
			user.setPassword(String.valueOf(random.nextInt(1231)));
			user.setSalt("");
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			userDao.addUser(user);
			user.setPassword("psw");
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

            //给每个资讯插几条评论Comment
			for (int j = 0; j < 3; j++) {
				Comment comment=new Comment();
				comment.setContent("我是一条评论"+i);
				comment.setCreateDate(new Date());
				comment.setEntityId(news.getId());
				comment.setEntityType(EntityType.ENTITY_NEWS);
				comment.setUserId(i+1);
				comment.setStatus(0);
				commentDao.addComment(comment);
			}

            //=======================LoginTicketService=========================
			LoginTicket loginTicket=new LoginTicket();
			loginTicket.setExpired(date);
			loginTicket.setStatus(0);
			loginTicket.setTicket(String.format("TICKET%d",i+1));
			loginTicket.setUserId(i+1);
			loginticketDao.addTicket(loginTicket);
			loginticketDao.updateStatus(1,loginTicket.getTicket());

			//=======================MessageDao=========================
			Message message=new Message();
			message.setContent("我是一条信息");
			message.setConversationId("1_2");
			message.setCreatedDate(new Date());
			message.setFromId(1);
			message.setToId(2);
			message.setHasRead(0);
			messageDao.addMessage(message);
		}
//		List<Message> messages=messageDao.getConversationList(1,0,10);
//        List<News> newsLIst=newsDao.selectByUserIdAndOffset(3,0,20);
//		userDao.deleteById(1);
//		Assert.assertNotNull(userDao.selectByUserId(1));

//		loginticketDao.deleteById(1);
//		Assert.assertNull(loginticketDao.selectByTicket("TICKET1"));


//		News news=newsDao.selectByUserId(2);
//		System.out.println(news);
//		List<Comment> commentList=commentDao.selectByEntity(1,1);
//		for(Comment comment:commentList)
//			System.out.println(comment);
		System.out.println(commentDao.getCommentCount(2,EntityType.ENTITY_NEWS));

		newsDao.updateComentCount(2,100);
	}

}
