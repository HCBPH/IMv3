package com.ycj.entity;

import java.io.Serializable;

/**
 * @author 53059
 * @date 2021/6/4 17:09
 */
public class Request implements Serializable {
    /**
     * 在发送请求之前，首先要建立连接(这都是在client和server之间的通信)
     * 连接分为两种一种是基础连接，即和服务器实现单用户通信，还有一种是组通信，服务器会转发组成员的消息。
     *
     * @TYPE 请求类别，创建组|加入组|退出组|其他请求
     * @GID 加入组的组号（点对点聊天默认在组0，其余组为用户创建的小组
     * @UID 请求者的用户ID
     * @KEY 对用户做一个识别
     */

    private int TYPE;
    private String GID;
    private String UID;
    private String KEY;
    private Object data;

    public Request() {
    }

    public Request(int TYPE, String UID, Object data, String KEY) {
        this.TYPE = TYPE;
        this.UID = UID;
        this.KEY = KEY;
        this.data = data;
    }

    public Request(int TYPE, String GID, String UID, String KEY) {
        this.TYPE = TYPE;
        this.GID = GID;
        this.UID = UID;
        this.KEY = KEY;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public String getGID() {
        return GID;
    }

    public void setGID(String GID) {
        this.GID = GID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
