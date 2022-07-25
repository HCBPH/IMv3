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
    private int type;
    private String group_profile;
    private ArrayList<User> users;
    private Hashtable<Long, Channel> channels;

    public Group(long id){
        this(id, "", 0L, "");
    }



    Group(long id, String name, long creator_id, String create_time){
        this.id = id;
        this.name = name;
        this.creator_id = creator_id;
        this.create_time = create_time;
        channels = new Hashtable<>();
    }

    public Group(long id, String name, long creator_id, String create_time, int type, String group_profile) {
        this.id = id;
        this.name = name;
        this.creator_id = creator_id;
        this.create_time = create_time;
        this.type = type;
        this.group_profile = group_profile;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGroup_profile() {
        return group_profile;
    }

    public void setGroup_profile(String group_profile) {
        this.group_profile = group_profile;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void add(Long uid, Channel ch, String username){
        this.add(uid, ch);
        this.add(new User(uid, username));
    }

    public void add(Long uid, Channel ch) {
        synchronized (this){
            if(channels == null){
                this.channels = new Hashtable<>();
            }
            this.channels.put(uid, ch);
        }
    }

    public void add(User user){
        synchronized (this){
            if(users == null){
                this.users = new ArrayList<>();
            }
            if(user != null){
                users.add(user);
            }
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

    public Hashtable<Long, Channel> getChannels() {
        return channels;
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
