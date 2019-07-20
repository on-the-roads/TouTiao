package com.chenjiawen.async;

import java.util.HashMap;

/**
 * 事件 Model
 * Created by jiawen.chen on 2019/7/20.
 */
public class EventModel {
    //事件类型
    private EventType eventType;
    //事件触发者
    private int actorId;
    //触发目标
    private int entityOwnerId;
    //实体类型
    private int entityType;
    //实体ID
    private int entityId;
    //触发信息保存Map
    private HashMap<String, String> exts = new HashMap<>();


    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }

    public EventModel() {

    }


    //set方法返回this引用，可以链式连续进行set方法，十分方便
    public EventModel set(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String get(String key) {
        return exts.get(key);
    }


    public EventType getEventType() {
        return eventType;
    }


    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getTargetId() {
        return entityOwnerId;
    }

    public EventModel setTargetId(int targetId) {
        entityOwnerId = targetId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public HashMap<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(HashMap<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
