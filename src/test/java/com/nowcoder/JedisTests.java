package com.nowcoder;

import com.ToutiaoApplication;
import com.chenjiawen.Dao.*;
import com.chenjiawen.Model.*;
import com.chenjiawen.Util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class JedisTests {

	@Autowired
	JedisAdapter jedisAdapter;
	@Test
	public void initData() {
		User user=new User();
		user.setPassword("123");
		user.setSalt(UUID.randomUUID().toString().substring(0,5));
		user.setId(90);
		user.setName("Jack");
		user.setHeadUrl("http://images.nowcoder.com/head/100t.png");
		jedisAdapter.setObject("user11x",user);
		User user1=jedisAdapter.getObject("user11x",User.class);
        System.out.println(ToStringBuilder.reflectionToString(user1));
	}

}
