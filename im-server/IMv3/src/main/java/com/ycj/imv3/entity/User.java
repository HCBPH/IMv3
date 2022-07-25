package com.ycj.imv3.entity;

/**
 * @author 53059
 * @date 2022/1/19 23:39
 */
public class User {
    private long uid;
    private String password;
    private String username;

    public User(long uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public User(long uid, String password, String username) {
        this.uid = uid;
        this.password = password;
        this.username = username;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
