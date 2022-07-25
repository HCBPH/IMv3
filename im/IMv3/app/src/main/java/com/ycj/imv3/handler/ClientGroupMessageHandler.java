package com.ycj.imv3.handler;

import android.util.Log;

import com.ycj.imv3.entity.GroupMessageOuterClass;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.starter.IMClient;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 53059
 * @date 2022/2/1 16:33
 */
public class ClientGroupMessageHandler extends GroupMessageHandler{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageOuterClass.GroupMessage msg) throws Exception {
        super.channelRead0(ctx, msg);
        Log.i("GroupMessageHandler", from + " --> " + to + "[" + new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date(timestamp)) + "]: " + content);
//        if(to == IMClient.getInstance().getUid()){
//            log.info(from + " --> " + to + "[" + new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date(timestamp)) + "]: " + content);
//        }else{
//            log.warn("from:"+from+"-->to:"+to+",发错人了");
//        }
    }
}
