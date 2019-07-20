package com.chenjiawen.Util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by jiawen.chen on 2019/7/13.
 */

@Component
public class JedisAdapter implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool jedisPool = null;

    /**
     * 创建线程池
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("localhost", 6379);
    }

    /**
     * 获取线程资源
     *
     * @return
     */
    private Jedis getResource() {
        return jedisPool.getResource();
    }


    public long sadd(String key, String val) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sadd(key, val);
        } catch (Exception e) {
            LOGGER.error("向集合中添加键值对失败 " + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public long srem(String key, String val) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.srem(key, val);
        } catch (Exception e) {
            LOGGER.error("向集合中删除键值对失败 " + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public boolean sismember(String key, String val) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.sismember(key, val);
        } catch (Exception e) {
            LOGGER.error("判断键值对是否在集合中失败 " + e.getMessage());
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            LOGGER.error("查询集合中指定键的数量失败 " + e.getMessage());
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }


    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            LOGGER.error("存储字符串失败" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public void setObject(String key, Object o) {
        set(key, JSON.toJSONString(o));
    }

    public <T> T getObject(String key,Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            String jsonObject=jedis.get(key);
            if(jsonObject!=null)
                return JSON.parseObject(jsonObject,clazz);
            return null;
        } catch (Exception e) {
            LOGGER.error("获取字符串失败" + e.getMessage());
            return null;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try{
            jedis=getResource();
            return jedis.lpush(key,value);
        }catch (Exception e)
        {
            LOGGER.error("向队列{}添加元素失败，value：{}"+e.getMessage(),key,value);
            return 0;
        }finally {
            if(jedis!=null)
                jedis.close();
        }
    }


    public List<String> brpop(String key) {
        Jedis jedis = null;
        try{
            jedis=getResource();
            return jedis.brpop(0,key);
        }catch (Exception e)
        {
            LOGGER.error("从队列{}取出元素失败"+e.getMessage(),key);
            return null;
        }finally {
            if(jedis!=null)
                jedis.close();
        }
    }
}
