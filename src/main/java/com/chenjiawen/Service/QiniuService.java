package com.chenjiawen.Service;

import com.alibaba.fastjson.JSONObject;
import com.chenjiawen.Util.ToutiaoUtil;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiniuService.class);

    //构造一个带指定Zone对象的配置类
    Configuration cfg = new Configuration(Zone.zone0());//华东机房
//...其他参数参考类注释

    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    String accessKey = "xowKnCRMHUVcPqQUzO3o8ySWjJwKSiQkuaUOun7R";
    String secretKey = "FkYeUu7t015d8p6TtZaWpCVlkxTU0sa2JEEOPi60";
    String bucket = "toutiao";
    //如果是Windows情况下，格式是 D:\\qiniu\\test.png
//    String localFilePath = "/home/qiniu/test.png";
    private static String QINIU_IMAGE_DOMAIN = "http://7xsetu.com1.z0.glb.clouddn.com/";//这个等七牛云权限申请下来要改

    //默认不指定key的情况下，以文件内容的hash值作为文件名
//    String key = null;

    Auth auth = Auth.create(accessKey, secretKey);

    public String getUpToken() {
        String upToken = auth.uploadToken(bucket);
        return upToken;
    }

    public String saveImage(MultipartFile file) {
        //获取文件名称
        String fileName = file.getOriginalFilename();
        //根据后缀名判断是否为图片文件
        int dosPos = fileName.lastIndexOf(".");
        if (dosPos < 0)
            return null;
        //获取后缀名的小写形式
        String ext = fileName.substring(dosPos + 1).toLowerCase();
        if (ToutiaoUtil.FileAllowed(ext)) {
            //对文件名利用UUID作转换
            fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + ext;
            //将图片文件比特流上传至七牛云存储
            Response response = null;
            try {
                response = uploadManager.put(file.getBytes(), fileName, getUpToken());
                //返回fileUrl
                if (response.isOK() && response.isJson())
                    return QiniuService.QINIU_IMAGE_DOMAIN + JSONObject.parseObject(response.bodyString()).get("key");//http请求的url形式
                else {
                    LOGGER.error("图片上传至七牛云失败:" + response.bodyString());
                    return null;
                }
            } catch (IOException e) {
                LOGGER.error("图片上传至七牛云失败:" + e.getMessage());
                return null;
            }
        }
        return null;
    }
}
