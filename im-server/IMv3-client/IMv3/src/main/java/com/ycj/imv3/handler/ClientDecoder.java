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
public class ClientDecoder extends Decoder{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.readBytes(separator);
        type = in.readInt();
        size = in.readInt();
        data = new byte[size];
        in.readBytes(data);
        Object msg = null;
//        log.info(separator[0]+"-"+separator[1]+"-"+type+"-"+size+"-"+data.length);
        if (type == IMConfiguration.TYPE_USER){
            msg = UserOuterClass.User.parseFrom(data);
        }else if(type == IMConfiguration.TYPE_INFO){
            msg = InfoOuterClass.Info.parseFrom(data);
        }else if(type == IMConfiguration.TYPE_LOGIN){
            msg = LoginOuterClass.Login.parseFrom(data);
        }else if(type == IMConfiguration.TYPE_RESPONSE){
            msg = ResponseOuterClass.Response.parseFrom(data);
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
    }
}
