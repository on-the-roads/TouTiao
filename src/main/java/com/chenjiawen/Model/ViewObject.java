package com.chenjiawen.Model;

import java.util.HashMap;

public class ViewObject {
    private HashMap<String,Object> hashMap=new HashMap<>();
    public Object get(String key)
    {
        return hashMap.get(key);
    }

    public void set(String key,Object obj)
    {
        hashMap.put(key, obj);
    }

}
