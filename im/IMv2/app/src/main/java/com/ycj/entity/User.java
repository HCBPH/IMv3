package com.ycj.entity;


import java.util.ArrayList;

import io.netty.channel.Channel;

/**
 * @author 53059
 * @date 2021/6/3 21:52
 */
public class User {

    /**
     * @UID 用户的唯一ID
     * @channel 用户和server之间的连接
     * @username 用户的用户名称
     * @friends 存放所有用户的好友UID
     * */

    private String UID;
    private Channel channel;
    private String username;
    private ArrayList<String> friends;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void addFriends(String uid) {
        this.friends.add(uid);
    }

    public void delFriends(String uid) {
        this.friends.remove(uid);
    }
}
