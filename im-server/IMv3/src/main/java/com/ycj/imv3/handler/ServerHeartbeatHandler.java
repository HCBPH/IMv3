package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.HeartbeatOuterClass;
import com.ycj.imv3.starter.IMServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 53059
 * @date 2022/1/29 19:08
 */
public class ServerHeartbeatHandler extends SimpleChannelInboundHandler<HeartbeatOuterClass.Heartbeat> {

    Logger log = LogManager.getLogger();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatOuterClass.Heartbeat msg) throws Exception {
        log.info("收到心跳------"+msg.getUid()+"("+msg.getLoca1()+","+msg.getLoca2()+")");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            ctx.channel().close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    ctx.close();
                    log.info(ctx.channel().remoteAddress()+"心跳停止，断开连接");
                }
            });

    }

}
