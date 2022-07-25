package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.listener.MessageListener;
import com.ycj.imv3.starter.IMClient;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 53059
 * @date 2022/1/25 14:43
 */
public class ClientMessageHandler extends MessageHandler{

    MessageListener listener;

    public ClientMessageHandler(MessageListener listener){
        this.listener = listener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageOuterClass.Message msg) throws Exception {
        super.channelRead0(ctx, msg);
        this.listener.receiveMessage(msg);
//        if(to == IMConfiguration.UID_SELF){
//            log.info(from + " --> " + to + "[" + new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date(timestamp)) + "]: " + content);
//        }else{
//            log.warn("from:"+from+"-->to:"+to+",发错人了");
//        }
    }
}
