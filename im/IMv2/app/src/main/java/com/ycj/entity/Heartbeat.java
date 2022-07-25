package com.ycj.entity;

import java.io.Serializable;

/**
 * @author 53059
 * @date 2021/7/12 23:00
 */
public class Heartbeat implements Serializable {

    private String uid;
    private String state;

    public Heartbeat(String uid, String state) {
        this.uid = uid;
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
