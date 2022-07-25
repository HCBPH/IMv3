package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.*;
import com.ycj.imv3.starter.IMServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author 53059
 * @date 2022/1/29 18:40
 */
public class ServerDecoder extends Decoder{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.readBytes(separator);
        type = in.readInt();
        size = in.readInt();
        data = new byte[size];
        in.readBytes(data);
        Object msg = null;
//        log.info(separator[0]+"-"+separator[1]+"-"+type+"-"+size+"-"+data.length);
        if(IMServer.getInstance().getUsers().contains(ctx.channel())){
            if (type == IMConfiguration.TYPE_USER){
                msg = UserOuterClass.User.parseFrom(data);
            }else if(type == IMConfiguration.TYPE_INFO){
                msg = InfoOuterClass.Info.parseFrom(data);
            }else if(type == IMConfiguration.TYPE_MESSAGE){
                msg = MessageOuterClass.Message.parseFrom(data);
            }else if(type == IMConfiguration.TYPE_MESSAGE_GROUP){
                msg = GroupMessageOuterClass.GroupMessage.parseFrom(data);
            }else if(type == IMConfiguration.TYPE_HEARTBEAT){
                msg = HeartbeatOuterClass.Heartbeat.parseFrom(data);
            }else{
                log.warn("解码器解析错误!");
                return;
            }
            out.add(msg);
        }else if(type == IMConfiguration.TYPE_LOGIN){
            msg = LoginOuterClass.Login.parseFrom(data);
            out.add(msg);
        }else{
            log.warn("非法用户发送信息");

            ResponseOuterClass.Response res = ResponseOuterClass.Response.newBuilder()
                    .setFrom(IMConfiguration.UID_SERVER)
                    .setTo(IMConfiguration.UID_SERVER)
                    .setType(IMConfiguration.RESPONSE_TYPE_NO_LOGIN)
                    .setContent("用户不存在")
                    .setTimestamp(System.currentTimeMillis())
                    .build();
            ctx.writeAndFlush(res);
        }

    }

}
