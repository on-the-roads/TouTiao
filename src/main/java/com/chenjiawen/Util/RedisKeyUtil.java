package com.chenjiawen.Util;

/**
 * 构建指定格式的key
 * Created by jiawen.chen on 2019/7/19.
 */
public class RedisKeyUtil {
    private static String LIKE = "LIKE";
    private static String DISLIKE = "DISLIKE";
    private static String SPLIT = ":";
    private static String BIZEVENT = "EVENT";

    public static String getLikeKey(int entityType, int entityId) {
        return LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId) {
        return DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    /**
     * 事件队列名称
     * @return
     */
    public static String getEventQueue() {
        return BIZEVENT;
    }
}
