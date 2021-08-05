package com.deng.alarmclocknote.dao;

import java.util.HashMap;

public class MessageEvent {

    private HashMap<String,Object> map;

    public MessageEvent(HashMap<String,Object> map){
        setMap(map);
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Object> map) {
        this.map = map;
    }
}
