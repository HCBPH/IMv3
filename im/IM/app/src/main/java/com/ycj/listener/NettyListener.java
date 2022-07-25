package com.ycj.listener;


import com.ycj.entity.Message;
import com.ycj.entity.Response;

public interface NettyListener {
    public void onStateChange(int code);

    public void onReceiveMsg(Message msg);

    public void onReceiveMsg(Response response);
}
