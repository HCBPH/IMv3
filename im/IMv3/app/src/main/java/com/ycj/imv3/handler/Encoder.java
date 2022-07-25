package com.ycj.imv3.handler;

import android.util.Log;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author 53059
 * @date 2022/1/14 17:33
 */
public class Encoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
//        log.info("encode");
        int type = 0;
        byte[] content;

        if (in instanceof UserOuterClass.User){
            type = IMConfiguration.TYPE_USER;
            content = ((UserOuterClass.User) in).toByteArray();
        }else if (in instanceof InfoOuterClass.Info){
            type = IMConfiguration.TYPE_INFO;
            content = ((InfoOuterClass.Info) in).toByteArray();
        }else if (in instanceof LoginOuterClass.Login){
            type = IMConfiguration.TYPE_LOGIN;
            content = ((LoginOuterClass.Login) in).toByteArray();
        }else if (in instanceof MessageOuterClass.Message){
            type = IMConfiguration.TYPE_MESSAGE;
            content = ((MessageOuterClass.Message) in).toByteArray();
//            log.info(in.toString());
        }else if (in instanceof GroupMessageOuterClass.GroupMessage){
            type = IMConfiguration.TYPE_MESSAGE_GROUP;
            content = ((GroupMessageOuterClass.GroupMessage) in).toByteArray();
//            log.info(in.toString());
        }else if (in instanceof HeartbeatOuterClass.Heartbeat){
            type = IMConfiguration.TYPE_HEARTBEAT;
            content = ((HeartbeatOuterClass.Heartbeat) in).toByteArray();
//            log.info(in.toString());
        }else{
            Log.e("Encoder", "编码器编译错误!");
            return;
        }

        out.writeBytes(IMConfiguration.SEPARATOR);
        out.writeInt(type);
        out.writeInt(content.length);
        out.writeBytes(content);
    }
}
