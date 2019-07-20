package com.chenjiawen.Controller;

import com.chenjiawen.Model.*;
import com.chenjiawen.Service.LikeService;
import com.chenjiawen.Service.NewsService;
import com.chenjiawen.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);


    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {
        List<ViewObject> objectList = getNews(0, 0, 10);
        model.addAttribute("vos", objectList);
        model.addAttribute("pop", pop);
        LOGGER.info("展示home页面");
        return "home";
    }

    @RequestMapping(path = {"/user/{id}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String UserIndex(Model model, @PathVariable("id") int userId) {
        List<ViewObject> objectList = getNews(userId, 0, 10);
        model.addAttribute("vos", objectList);
        LOGGER.info("展示指定用户ID{}页面", userId);
        return "home";
    }


    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getUserIdAndOffset(userId, offset, limit);
        List<ViewObject> objectList = new ArrayList<>();
        User localUser = hostHolder.getUser();
        for (News New : newsList) {
            ViewObject viewObject = new ViewObject();
            viewObject.set("news", New);
            viewObject.set("user", userService.getUserBYid(New.getId()));
            //设置当前用户下每条资讯的喜欢状态，用于前端显示
            if (localUser != null)
                viewObject.set("like", likeService.getLikeStatus(localUser.getId(), EntityType.ENTITY_NEWS, New.getId()));
            else
                viewObject.set("like", 0);
            objectList.add(viewObject);
        }
        return objectList;
    }
}
