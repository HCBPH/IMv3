package com.ycj.imv3.handler;

import com.ycj.imv3.entity.ResponseOuterClass;
import com.ycj.imv3.listener.ResponseListener;
import com.ycj.imv3.starter.IMClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseHandler extends SimpleChannelInboundHandler<ResponseOuterClass.Response> {

    Logger log = LogManager.getLogger();
    ResponseListener listener;

    long from;
    long to;
    int type;
    String content;
    long timestamp;

    public ResponseHandler(ResponseListener listener){
        this.listener = listener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseOuterClass.Response msg) throws Exception {
        from = msg.getFrom();
        to = msg.getTo();
        type = msg.getType();
        content = msg.getContent();
        timestamp = msg.getTimestamp();
        listener.receiveResponse(msg);
//        log.info("服务器回应:"+type+"----"+content);
    }
}
