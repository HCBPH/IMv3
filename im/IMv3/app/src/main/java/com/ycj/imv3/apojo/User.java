package com.ycj.imv3.apojo;

public class User {
    private long uid;
    private String username;
    private String profile; // user头像的缓存路径或者restful链接
    private String lastInfo;  // 类似个性签名
    private String lastMsg;  // 最近发送的消息
    private long lastInfoTime;
    private long lastMsgTime;

    public User(long uid, String username){
        this.uid = uid;
        this.username = username;
    }

    public User(long uid, String username, String lastMsg){
        this.uid = uid;
        this.username = username;
        this.lastMsg = lastMsg;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLastInfo() {
        return lastInfo;
    }

    public void setLastInfo(String lastInfo) {
        this.lastInfo = lastInfo;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public long getLastInfoTime() {
        return lastInfoTime;
    }

    public void setLastInfoTime(long lastInfoTime) {
        this.lastInfoTime = lastInfoTime;
    }

    public long getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }
}
