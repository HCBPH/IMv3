package com.ycj.entity;

import java.io.Serializable;

/**
 * @author 53059
 * @date 2021/6/4 19:03
 */
public class Response implements Serializable {
    /**
     * 当用户发出请求，回复response
     *
     * @UID 回复的目标
     * @state 应答的内容
     * @data 其他数据
     */

    private String UID;
    private String state;
    private Object data;

    public Response() {
    }

    public Response(String UID, String state, Object data) {
        this.UID = UID;
        this.state = state;
        this.data = data;
    }

    public Response(String UID, String state) {
        this.UID = UID;
        this.state = state;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Response{" +
                "UID='" + UID + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
