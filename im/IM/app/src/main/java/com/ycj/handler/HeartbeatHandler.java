package com.ycj.handler;

import android.util.Log;

import com.ycj.client.NettyClient;
import com.ycj.common.Common;
import com.ycj.entity.Group;
import com.ycj.entity.Heartbeat;
import com.ycj.entity.Request;
import com.ycj.entity.User;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author 53059
 * @date 2021/7/12 23:05
 */
public class HeartbeatHandler extends SimpleChannelInboundHandler<Heartbeat> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.d("HeartbeatHandler", "active");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Heartbeat msg) throws Exception {
        Log.d("HeartbeatHandler", msg.getUid()+"|"+msg.getState());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
//                ctx.channel().writeAndFlush(new Request(0, "2", Common.UID, ""));
                ctx.channel().writeAndFlush(new Heartbeat(NettyClient.getUID(), "heartbeat"));
                Log.d("HeartbeatHandler", "heartbeat");
            }
        }
    }
}
