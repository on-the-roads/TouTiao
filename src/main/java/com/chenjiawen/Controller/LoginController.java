package com.chenjiawen.Controller;

import com.chenjiawen.Model.EntityType;
import com.chenjiawen.Service.UserService;
import com.chenjiawen.Util.ToutiaoUtil;
import com.chenjiawen.async.EventModel;
import com.chenjiawen.async.EventProducer;
import com.chenjiawen.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;


    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(path = "/reg/")
    @ResponseBody
    public String Register( @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "rember", defaultValue = "0") int remmberMe, HttpServletResponse response) {
        Map<String, Object> regMap = null;
        regMap = userService.register(username, password);
        try {
            if (regMap.isEmpty()) {
                return ToutiaoUtil.getJsonString(0, "注册成功！");
            }
            return ToutiaoUtil.getJsonString(1, regMap);
        } catch (Exception e) {
            LOGGER.error("注册失败", e);
            return ToutiaoUtil.getJsonString(1, "注册失败");
        }
    }

    @RequestMapping(path = "/login/")
    @ResponseBody
    public String Login( @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int remmberMe, HttpServletResponse response) {
        Map<String, Object> loginMap = null;
        try {
            LOGGER.info("用户尝试登录,username:{},password:{}",username,password);
            loginMap = userService.login(username, password);
            if (loginMap.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", loginMap.get("ticket").toString());
                cookie.setPath("/");//设置cookie全路径
                if (remmberMe > 0)
                    cookie.setMaxAge(3600 * 24 * 5);//单位：s,  记录登陆状态，则将cookie有效期设为更长
                response.addCookie(cookie);

                int userId= (int) loginMap.get("userId");
                eventProducer.MakeEvent(new EventModel(EventType.LOGIN)
                        .set("userName",username)
                        .set("email","123456789@qq.com")
                        .setActorId(userId)
                        .setTargetId(userId)
                        .setEntityId(EntityType.ENTITY_NEWS));
                return ToutiaoUtil.getJsonString(0, "登陆成功！");
            }
            return ToutiaoUtil.getJsonString(1, loginMap);
        } catch (Exception e) {
            LOGGER.error("登陆失败", e);
            return ToutiaoUtil.getJsonString(1, "登陆失败");
        }
    }


    @RequestMapping(path = "/logout/")
    public String Logout(@CookieValue("ticket")String ticket ){
        userService.logout(ticket);
        return "redirect:/";
    }
}
