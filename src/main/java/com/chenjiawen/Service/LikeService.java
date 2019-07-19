package com.chenjiawen.Service;

import com.chenjiawen.Util.JedisAdapter;
import com.chenjiawen.Util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jiawen.chen on 2019/7/19.
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long like(int userId, int entityType, int entityId) {
        //向喜欢集合中添加
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        //从不喜欢集合中删除
        jedisAdapter.srem(RedisKeyUtil.getDisLikeKey(entityType, entityId), String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long dislike(int userId, int entityType, int entityId) {
        //向不喜欢集合中添加
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        //从喜欢集合中删除
        jedisAdapter.srem(RedisKeyUtil.getLikeKey(entityType, entityId), String.valueOf(userId));
        return jedisAdapter.scard(disLikeKey);
    }


    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId)))
            return 1;
        return jedisAdapter.sismember(dislikeKey, String.valueOf(userId)) ? -1 : 0;
    }


}
