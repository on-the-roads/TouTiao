package com.chenjiawen.Controller;

import com.chenjiawen.Model.EntityType;
import com.chenjiawen.Model.HostHolder;
import com.chenjiawen.Service.LikeService;
import com.chenjiawen.Service.NewsService;
import com.chenjiawen.Util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by jiawen.chen on 2019/7/19.
 */
@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    NewsService newsService;
    @Autowired
    LikeService likeService;
    private static final Logger LOGGER= LoggerFactory.getLogger(LikeController.class);

    @RequestMapping(path = "/like")
    @ResponseBody
    public String Like(@RequestParam("newsId") int newsId) {
        try {
            int userId = hostHolder.getUser().getId();
            long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
            newsService.updateLikeCount(newsId,(int)likeCount);
            return ToutiaoUtil.getJsonString(0, "喜欢个数: "+String.valueOf(likeCount));
        } catch (Exception e) {
            LOGGER.error("添加喜欢失败，newsId={] "+e.getMessage(),newsId);
            return ToutiaoUtil.getJsonString(1, "添加喜欢失败");
        }
    }


    @RequestMapping(path = "/dislike")
    @ResponseBody
    public String DisLike(@RequestParam("newsId") int newsId) {
        try {
            int userId = hostHolder.getUser().getId();
            long dislikeCount = likeService.dislike(userId, EntityType.ENTITY_NEWS, newsId);
            newsService.updateLikeCount(newsId,(int)dislikeCount);
            return ToutiaoUtil.getJsonString(0, "不喜欢个数: "+String.valueOf(dislikeCount));
        } catch (Exception e) {
            LOGGER.error("添加不喜欢失败，newsId={] "+e.getMessage(),newsId);
            return ToutiaoUtil.getJsonString(1, "添加不喜欢失败");
        }
    }
}

