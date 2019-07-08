package com.chenjiawen.Service;

import com.chenjiawen.Dao.NewsDao;
import com.chenjiawen.Model.News;
import com.chenjiawen.Util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    public List<News> getUserIdAndOffset(int userId,int offset,int limit){
        List<News> newsList=newsDao.selectByUserIdAndOffset(userId,offset,limit);
        return newsList;
    }

    public News getNewsById(int userId)
    {
        return newsDao.selectByUserId(userId);
    }
    public int addNews(News news)
    {
        return newsDao.addNews(news);
    }

    public String saveImage(MultipartFile file) throws IOException {
        //获取文件名称
        String fileName=file.getOriginalFilename();
        //根据后缀名判断是否为图片文件
        int dosPos=fileName.lastIndexOf(".");
        if(dosPos<0)
            return null;
        //获取后缀名的小写形式
        String ext=fileName.substring(dosPos+1).toLowerCase();
        if(ToutiaoUtil.FileAllowed(ext))
        {
            //对文件名利用UUID作转换
            fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+ext;
            //将图片文件二进制流保存至本地
            Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            //返回fileUrl
            return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+fileName;//http请求的url形式
        }
        return null;
    }
}
