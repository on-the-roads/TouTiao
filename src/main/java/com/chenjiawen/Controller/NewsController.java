package com.chenjiawen.Controller;

import com.chenjiawen.Service.NewsService;
import com.chenjiawen.Service.QiniuService;
import com.chenjiawen.Util.ToutiaoUtil;
import com.sun.deploy.net.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    QiniuService qiniuService;


    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @RequestMapping(path = "/uploadImage/",method = {RequestMethod.POST})
    @ResponseBody
    public String UploadImage(@RequestParam(value = "file") MultipartFile file){
        try {
            //保存图片字节流到本地服务器
            String fileUrl = newsService.saveImage(file);
            //上传图片至七牛云存储
//            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null)
                return ToutiaoUtil.getJsonString(1, "上传图片错误！");
            return ToutiaoUtil.getJsonString(0,fileUrl);
        }catch (Exception  e)
        {
            LOGGER.error("上传图片错误！");
            return ToutiaoUtil.getJsonString(1,"上传图片错误");
        }
    }

    @RequestMapping(path = "/image",method ={RequestMethod.GET})
    @ResponseBody
    public void ShowImage(@RequestParam(value = "name") String  imageName, HttpServletResponse response){
                response.setContentType("image/jpeg");
        try {
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)),response.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("读取图片失败，"+e.getMessage());
        }
    }
}
