package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.InfoOuterClass;
import com.ycj.imv3.entity.LoginOuterClass;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.entity.UserOuterClass;
import com.ycj.imv3.starter.IMServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author 53059
 * @date 2022/1/14 17:34
 */
public class Decoder extends ByteToMessageDecoder {

    Logger log = LogManager.getLogger();

    byte[] separator = new byte[2];
    int type;
    int size;
    byte[] data;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
