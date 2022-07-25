package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.InfoOuterClass;
import com.ycj.imv3.entity.LoginOuterClass;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.entity.UserOuterClass;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author 53059
 * @date 2022/1/14 17:34
 */
public class Decoder extends ByteToMessageDecoder {

    byte[] separator = new byte[2];
    int type;
    int size;
    byte[] data;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
