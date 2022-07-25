package com.ycj.imv3.handler;

import com.ycj.imv3.entity.Group;
import com.ycj.imv3.entity.GroupMessageOuterClass;
import com.ycj.imv3.entity.MessageOuterClass;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 53059
 * @date 2022/1/14 17:40
 */
public class GroupMessageHandler extends SimpleChannelInboundHandler<GroupMessageOuterClass.GroupMessage> {

    Logger log = LogManager.getLogger();
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
