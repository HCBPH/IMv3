package com.ycj.imv3.handler;

import com.ycj.imv3.entity.MessageOuterClass;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author 53059
 * @date 2022/1/14 17:40
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageOuterClass.Message> {

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
