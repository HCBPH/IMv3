package com.ycj.imv3.handler;

import android.util.Log;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.starter.IMClient;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 53059
 * @date 2022/1/25 14:43
 */
public class ClientMessageHandler extends MessageHandler{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageOuterClass.Message msg) throws Exception {
        super.channelRead0(ctx, msg);
        if(to == IMConfiguration.UID_SELF){
            Log.i("MessageHandler", from + " --> " + to + "[" + new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date(timestamp)) + "]: " + content);
        }else{
            Log.w("MessageHandler", "from:"+from+"-->to:"+to+",发错人了");
        }
    }
}
