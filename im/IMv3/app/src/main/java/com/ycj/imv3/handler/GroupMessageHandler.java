package com.ycj.imv3.handler;

import com.ycj.imv3.entity.Group;
import com.ycj.imv3.entity.GroupMessageOuterClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author 53059
 * @date 2022/1/14 17:40
 */
public class GroupMessageHandler extends SimpleChannelInboundHandler<GroupMessageOuterClass.GroupMessage> {

    long from;
    long to;
    String content;
    long timestamp;
    Group dstGroup;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageOuterClass.GroupMessage msg) throws Exception {
        from = msg.getFrom();
        to = msg.getTo();
        content = msg.getContent();
        timestamp = msg.getTimestamp();
    }
}
