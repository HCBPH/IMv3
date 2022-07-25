package com.ycj.handler;


import android.util.Log;

import com.ycj.entity.Message;
import com.ycj.listener.NettyListener;

import io.netty.channel.*;

import java.text.SimpleDateFormat;


/**
 * @author 53059
 * @date 2021/5/29 12:23
 */
public class MessageHandler extends SimpleChannelInboundHandler<Message> {

    NettyListener listener;

    public void setListener(NettyListener listener) {
        this.listener = listener;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        listener.onStateChange(0);
    }

    public MessageHandler(NettyListener listener){
        this.listener = listener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        listener.onReceiveMsg(msg);
        Log.d("MessageHandler","["+msg.getSrc()+"]-->"+msg.getData());
    }

}
