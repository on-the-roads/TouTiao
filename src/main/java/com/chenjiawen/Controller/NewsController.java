package com.chenjiawen.Controller;

import com.chenjiawen.Model.*;
import com.chenjiawen.Service.CommentService;
import com.chenjiawen.Service.NewsService;
import com.chenjiawen.Service.QiniuService;
import com.chenjiawen.Service.UserService;
import com.chenjiawen.Util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    //上传图片类型文件
    @RequestMapping(path = "/uploadImage/", method = {RequestMethod.POST})
    @ResponseBody
    public String UploadImage(@RequestParam(value = "file") MultipartFile file) {
        try {
            //保存图片字节流到本地服务器
//            String fileUrl = newsService.saveImage(file);
            //上传图片至七牛云存储
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null)
                return ToutiaoUtil.getJsonString(1, "上传图片错误！");
            return ToutiaoUtil.getJsonString(0, fileUrl);
        } catch (Exception e) {
            LOGGER.error("上传图片错误！");
            return ToutiaoUtil.getJsonString(1, "上传图片错误");
        }
    }

    //展示资讯详情
    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.POST,RequestMethod.GET})
    public String newsDetail(Model model, @PathVariable(value ="newsId")int newsId){

       try{
           News  news=newsService.getNewsById(newsId);
           //对资讯做处理,add comments
           if(news!=null)
           {
               List<Comment> comments=commentService.getCommentList(newsId,EntityType.ENTITY_NEWS);
               List<ViewObject> commentVos=new ArrayList<>();
               for(Comment comment:comments)
               {
                   ViewObject commentVo=new ViewObject();
                   commentVo.set("user",userService.getUserBYid(comment.getUserId()));
                   commentVo.set("comment",comment);
                   commentVos.add(commentVo);
               }

               model.addAttribute("comments",commentVos);
           }
           model.addAttribute("news",news);
           model.addAttribute("owner",userService.getUserBYid(newsId));

       }catch (Exception e)
       {
           LOGGER.error("获取资讯详情页错误"+e.getMessage());
       }
        return "detail";//跳转到资讯详情页
    }

    //添加资讯
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        News news = new News();
        if (hostHolder.getUser() != null)
            news.setUserId(hostHolder.getUser().getId());
        //匿名用户Id统一设置为0
        else news.setUserId(ToutiaoUtil.ANONYMOUS_USERS);
        news.setImage(image);
        news.setTitle(title);
        news.setLink(link);
        news.setCreatedDate(new Date());

        try{
            newsService.addNews(news);
            return ToutiaoUtil.getJsonString(0);
        }catch (Exception e)
        {
            LOGGER.error("插入资讯失败："+e.getMessage());
            return ToutiaoUtil.getJsonString(1,"插入资讯失败");
        }
    }

    //添加资讯下对应的评论
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST,RequestMethod.GET})
    public String addComment(@RequestParam("newsId")int newsId,@RequestParam ("content")String content){
        try {
            Comment comment = new Comment();
            comment.setStatus(0);
            comment.setUserId(hostHolder.getUser().getId());
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setContent(content);
            comment.setCreateDate(new Date());
            commentService.addComment(comment);

            //更新评论数量
            int commmentCount=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateComentCount(comment.getEntityId(),commmentCount);
            //如何异步化
        }catch(Exception e){
            LOGGER.error("添加评论错误"+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }

    //查看图片
    @RequestMapping(path = "/image", method = {RequestMethod.GET})
    @ResponseBody
    public void ShowImage(@RequestParam(value = "name") String imageName, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        try {
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("读取图片失败，" + e.getMessage());
        }
    }
}
