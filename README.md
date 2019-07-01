## 头条
仿照今日头条的主页toutiao.com做的一个Java web项目。使用SpringBoot+Mybatis+velocity开发。       

## 功能实现        
1. SpringBoot初始化配置             
2. 搭建基本框架      
2.1 写sql语句创建表，创建对应的model     
2.2 Resources目录下添加mybatis-config.xml文件   
2.3 修改application.properties配置文件：增加数据源配置信息以及添加mybatis配置文件地址    
2.4 创建基本的Controller层，Service层，Dao层       
2.5 aspect实现切面，并且使用logger来记录日志，用该切面的切面方法来监听controller。   
       