## 头条
仿照今日头条的主页toutiao.com做的一个Java web项目。使用SpringBoot+Mybatis+velocity开发。       

## 功能实现        
1 SpringBoot初始化配置             
2 搭建基本框架
>      
2.1 写sql语句创建表，创建对应的model     
2.2 Resources目录下添加mybatis-config.xml文件   
2.3 修改application.properties配置文件：增加数据源配置信息以及添加mybatis配置文件地址    
2.4 创建基本的Controller层，Service层，Dao层       
2.5 aspect实现切面，并且使用logger来记录日志，用该切面的切面方法来监听controller。
    
3 添加home页面以及个人展示页，编写对应的Controller方法  
4  添加注册功能   
>注册的结果封装在map中返回，Service层中注册逻辑为：  
>1） 校验用户名   
>2） 校验密码  
>3） 验证用户是否已存在   
>4） 对新用户密码进行加盐处理   
>在这个过程中用到了MD5加密技术和JSON字符串转换技术，创建一个TouTiaoUtil包便于使用   

5 添加登陆功能
> 在登录的逻辑中添加LoginTicket用作用户登录标识   
> 当用户账户名和密码验证成功后，向ticket表中插入ticket记录，同时在Cookie中添加ticket

6 设置拦截器用于对非法访问用户进行拦截
> 编写拦截器：写一个Interceptor类继承HandlerInterceptor，覆写其中的preHandle等方法  
> 配置拦截器：写一个类继承WebMvcConfigurationAdapter，覆写其中的addInterceptors方法，拦截器顺序与代码先后顺序保持一致    
> 以LoginController为例，preHandle逻辑为：   
> 1）校验cookies中的ticket是否存在   
> 2）检查ticket是否过期   
> 3）根据LoginTicket的userID查询用户  
> 4）编写一个HostHolder类，用ThreadLocal来保存当前线程下的User信息，注意在postHandle中删除user信息  

7 添加注销功能   
>主要通过改变用户对应的ticket的status来实现，当status值非0时用户登录状态失效   

8 添加文件上传功能和文件查看功能
>8.1 尝试使用dev-tools工具，能够实现动态编译，编译有改动的文件即可，不需要重新启动SpringBoot   
>8.2 添加图片类型文件上传功能（POST请求）   
>1)获取文件名称  
>2)根据文件后缀名判断是否为图片   
>3)保存到本地服务器或者云存储   
>4)返回图片对应的http请求url地址    
>8.3 添加图片查看功能（GET请求）  
>1)获取图片字节流   
>2)利用response的getOutputStream输出   
>8.4 上传图片保存在本地功能测试成功后，尝试将图片上传至云存储  
>云储存的优点：冗余备份，统一访问，CDN缓存同步，多机器并发，实时缩图（通过在图片的请求url后面配置尺寸规格）等    

  
9 添加资讯上传功能（传入参数：云端图片url地址，资讯名称，资讯链接）和展示资讯详情页   

 
       