package com.ycj.imv3.handler;

import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.starter.IMServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 53059
 * @date 2022/1/14 17:40
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageOuterClass.Message> {

    Logger log = LogManager.getLogger();
    long from;
    long to;
    String content;
    long timestamp;
    Channel dstChannel;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageOuterClass.Message msg) throws Exception {
        from = msg.getFrom();
        to = msg.getTo();
        content = msg.getContent();
        timestamp = msg.getTimestamp();
    }
}
