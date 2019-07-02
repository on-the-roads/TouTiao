package com.chenjiawen.Controller;

import com.chenjiawen.Aspect.LogAspect;
import com.chenjiawen.Model.News;
import com.chenjiawen.Model.ViewObject;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    private static final Logger LOGGER= LoggerFactory.getLogger(HomeController.class);


    @RequestMapping(path = {"/","/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        List<ViewObject> objectList=getNews(0,0,10);
        model.addAttribute("vos",objectList);
        LOGGER.info("登录home页面");
        return "home";
    }

    @RequestMapping(path = {"/user/{id}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String UserIndex(Model model,@PathVariable("id")int userId) {
        List<ViewObject> objectList=getNews(userId,0,10);
        model.addAttribute("vos",objectList);
        LOGGER.info("登录指定用户ID{}页面",userId);
        return "home";
    }



    private List<ViewObject> getNews(int userId,int offset,int limit){
        List<News> newsList=newsService.getUserIdAndOffset(userId,offset,limit);
        List<ViewObject> objectList=new ArrayList<>();
        for(News New:newsList)
        {
            ViewObject viewObject=new ViewObject();
            viewObject.set("news",New);
            viewObject.set("user",userService.getUserBYid(New.getId()));
            objectList.add(viewObject);
        }
        return objectList;
    }
}