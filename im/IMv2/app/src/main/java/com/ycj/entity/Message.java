package com.ycj.entity;

import java.io.Serializable;

/**
 * @author 53059
 * @date 2021/5/29 10:52
 */
public class Message implements Serializable {
    /**
     * @tag 0单发（UID生效）|1组发（GID生效）
     * @type 数据类型 0文本  1非文本
     * @src 消息源，用户ID（唯一识别）
     * @dst 目的UID|目的GID
     * @username 用户名称
     * @timestamp System.currentTimeMillis()，实现消息的时序性
     * @size 发送数据的大小
     * @data 数据段
     */

    private int tag;
    private int type;
    private String src;
    private String dst;
    private String username;
    private Long timestamp;
    private int size;
    private Object data;

    public Message() {
    }

    public Message(int tag, int type, String src, String dst, String username, Long timestamp, Object data) {

        this.tag = tag;
        this.type = type;
        this.src = src;
        this.dst = dst;
        this.username = username;
        this.timestamp = timestamp;
        this.data = data;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dest) {
        this.dst = dest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
