package com.ycj.handler;

import android.util.Log;

import com.ycj.entity.Response;
import com.ycj.listener.NettyListener;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * @author 53059
 * @date 2021/6/4 23:14
 */
public class ResponseHandler extends SimpleChannelInboundHandler<Response> {

    NettyListener listener;

    public void setListener(NettyListener listener) {
        this.listener = listener;
    }

    public ResponseHandler(NettyListener listener){
        this.listener = listener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        listener.onReceiveMsg(msg);
        Log.d("ResponseHandler" ,msg.getState());
        Map<String, Integer> data = (Map<String, Integer>) msg.getData();
        if (data != null){
            System.out.println(data.toString());
        }
    }
}
