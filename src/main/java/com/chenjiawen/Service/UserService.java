package com.chenjiawen.Service;

import com.chenjiawen.Dao.LoginticketDao;
import com.chenjiawen.Dao.UserDao;
import com.chenjiawen.Model.LoginTicket;
import com.chenjiawen.Model.User;
import com.chenjiawen.Util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginticketDao loginticketDao;

    public  void logout(String ticket) {
        loginticketDao.updateStatus(1,ticket);
    }

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> regmap = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            regmap.put("msgname", "用户名不能为空");
            return regmap;
        }
        if (StringUtils.isBlank(password)) {
            regmap.put("msgpwd", "密码不能为空");
            return regmap;
        }
        User user=userDao.selectByname(username);
        if(user!=null) {
            regmap.put("msgexists", "该用户已经被注册！");
            return regmap;
        }
        User newuser=new User();
        newuser.setName(username);
        newuser.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        newuser.setSalt(UUID.randomUUID().toString().substring(0,5));
        newuser.setPassword(ToutiaoUtil.MD5(newuser.getSalt()+password));
        userDao.addUser(newuser);

        return regmap;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> loginMap = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            loginMap.put("msgname", "用户名不能为空");
            return loginMap;
        }
        if (StringUtils.isBlank(password)) {
            loginMap.put("msgpwd", "密码不能为空");
            return loginMap;
        }
        User user=userDao.selectByname(username);
        if(user==null) {
            loginMap.put("msgexists", "该用户不存在！");
            return loginMap;
        }
        if(!ToutiaoUtil.MD5(user.getSalt()+password).equals(userDao.selectByname(username).getPassword()))
        {
            loginMap.put("msgPwdUnmatch", "密码不正确！");
            return loginMap;
        }
        String ticket=addLoginticket(user.getId());
        loginMap.put("ticket",ticket);
        return loginMap;

    }

    private String addLoginticket(int userId) {
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setStatus(0);
        loginTicket.setUserId(userId);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        Date data=new Date();
        data.setTime(data.getTime()+1000*3600*24);//单位：ms
        loginTicket.setExpired(data);//ticket有效期设为24小时
        loginticketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public User getUserBYid(int id) {
        User user = userDao.selectById(id);
        return user;
    }


}
