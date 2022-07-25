package com.ycj.entity;

import java.util.HashMap;

/**
 * @author 53059
 * @date 2021/6/4 19:33
 */
public class Group {
    private String GID;
    private HashMap<String, User> members;
    private int size;

    public Group(String GID) {
        this.GID = GID;
        this.size = 0;
    }

    public String getGID() {
        return GID;
    }

    public void setGID(String GID) {
        this.GID = GID;
    }

    public int getSize() {
        return size;
    }

    public HashMap<String, User> getMembers() {
        return members;
    }

    public void addMembers(String uid, User user) {
        if (members == null) {
            members = new HashMap<>();
        }
        this.members.put(uid, user);
        this.size ++;
    }

    public void removeMember(String uid) {
        this.members.remove(uid);
        this.size--;
    }
}
