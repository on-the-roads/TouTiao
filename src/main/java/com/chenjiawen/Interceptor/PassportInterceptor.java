package com.chenjiawen.Interceptor;

import com.chenjiawen.Dao.LoginticketDao;
import com.chenjiawen.Dao.UserDao;
import com.chenjiawen.Model.HostHolder;
import com.chenjiawen.Model.LoginTicket;
import com.chenjiawen.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    LoginticketDao loginticketDao;
    @Autowired
    UserDao userDao;
    @Autowired
    HostHolder hostHolder;

    private static final Logger LOGGER= LoggerFactory.getLogger(PassportInterceptor.class);

    //true继续请求，false拒绝请求
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        LOGGER.debug("拦截器preHandle");
        String ticket=null;
        Cookie[] cookies=httpServletRequest.getCookies();
        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket!=null)
        {
            LoginTicket loginTicket=loginticketDao.selectByTicket(ticket);
            if(loginTicket==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0)//ticket的status用于判断ticket是否有效
                return true;
            User user= userDao.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);//当用户ticket有效时保存用户信息
//            LOGGER.info("用户登录验证成功，用户信息{}", user);
            return true;
        }
        return true;
    }

    //渲染之前提供的后处理方法，可以添加模型数据，自动传给前端。
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        LOGGER.debug("拦截器postHandle");
        if(modelAndView!=null&&hostHolder.getUser()!=null) {
            modelAndView.addObject("user",hostHolder.getUser());
            hostHolder.clearUser();
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
         LOGGER.debug("拦截器afterCompletion");

    }
}
