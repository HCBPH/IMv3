package com.ycj.imv3.handler;

import android.util.Log;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.HeartbeatOuterClass;
import com.ycj.imv3.starter.IMClient;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author 53059
 * @date 2022/1/29 19:08
 */
public class ClientHeartbeatHandler extends SimpleChannelInboundHandler<HeartbeatOuterClass.Heartbeat> {

    HeartbeatOuterClass.Heartbeat heartbeat;
    double loca1;
    double loca2;
    String location;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatOuterClass.Heartbeat msg) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        loca1 = 124.66;
        loca2 = 31.12;
        location = loca1 + "," + loca2;
        if (evt instanceof IdleStateEvent){
            heartbeat = HeartbeatOuterClass.Heartbeat.newBuilder()
                    .setState(IMConfiguration.TYPE_HEARTBEAT_HEALTHY)
                    .setUid(IMConfiguration.UID_SELF)
                    .setLoca1(loca1)
                    .setLoca2(loca2)
                    .setLocation(location)
                    .build();
            ctx.channel().writeAndFlush(heartbeat);
            Log.i("HeartbeatHandler", "发送心跳------("+loca1+","+loca2+")");
        }
    }
}
