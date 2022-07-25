package com.ycj.imv3.entity;

import io.netty.channel.Channel;

import java.util.*;

/**
 * @author 53059
 * @date 2022/1/30 10:44
 */
public class Group {
    private long id;
    private String name;
    private long creator_id;
    private String create_time;
    private ArrayList<User> users;
    private Hashtable<Long, Channel> channels;

    Group(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(long creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void add(Long uid, Channel ch) {
        synchronized (this){
            if(channels == null){
                this.channels = new Hashtable<>();
            }
            this.channels.put(uid, ch);
        }
    }

    public void remove(Long uid) {
        if (channels == null){
            return;
        }
        this.channels.remove(uid);
    }

    public boolean contains(Long uid){
        if (channels == null){
            return false;
        }
        return channels.containsKey(uid);
    }

    public boolean contains(Channel ch){
        if (channels == null){
            return false;
        }
        return channels.containsValue(ch);
    }

    public void send(GroupMessageOuterClass.GroupMessage msg) {
        final Collection<Channel> allchannel = channels.values();
        final Iterator<Channel> iterator = allchannel.stream().iterator();
        while(iterator.hasNext()){
            iterator.next().writeAndFlush(msg);
        }
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creator_id=" + creator_id +
                ", create_time='" + create_time + '\'' +
                '}';
    }
}
