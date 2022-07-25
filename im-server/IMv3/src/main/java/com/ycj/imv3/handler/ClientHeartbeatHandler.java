package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.HeartbeatOuterClass;
import com.ycj.imv3.entity.Location;
import com.ycj.imv3.listener.HeartbeatListener;
import com.ycj.imv3.starter.IMClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

/**
 * @author 53059
 * @date 2022/1/29 19:08
 */
public class ClientHeartbeatHandler extends SimpleChannelInboundHandler<HeartbeatOuterClass.Heartbeat> {

    HeartbeatListener listener;

    Logger log = LogManager.getLogger();
    HeartbeatOuterClass.Heartbeat heartbeat;
    String location;

    public ClientHeartbeatHandler(HeartbeatListener listener){
        this.listener = listener;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatOuterClass.Heartbeat msg) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        Location loc = listener.sendHeartbeat();
        location = loc.getLongitude() + "," + loc.getLatitude();
        if (evt instanceof IdleStateEvent){
            heartbeat = HeartbeatOuterClass.Heartbeat.newBuilder()
                    .setState(IMConfiguration.TYPE_HEARTBEAT_HEALTHY)
                    .setUid(IMConfiguration.UID_SELF)
                    .setLoca1(loc.getLongitude())
                    .setLoca2(loc.getLatitude())
                    .setLocation(location)
                    .build();
            ctx.channel().writeAndFlush(heartbeat);
            log.info("发送心跳------("+loc.getLongitude()+","+loc.getLatitude()+")");
        }
    }
}
