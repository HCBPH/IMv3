package com.ycj.imv3.handler;

import com.ycj.imv3.entity.MessageOuterClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 53059
 * @date 2022/1/14 15:08
 */
public class TestHandler extends SimpleChannelInboundHandler {

    Logger log = LogManager.getLogger();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        log.info("^^^^^^this is test^^^^^^");
        log.info(((MessageOuterClass.Message) o).getContent());
    }
}
