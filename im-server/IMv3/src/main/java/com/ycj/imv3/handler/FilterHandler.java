package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.util.Comparator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.ReferenceCountUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 53059
 * @date 2022/1/14 15:02
 */

/*
发送过来的bytebuf统一格式为:
   ----------------------------------------
  | separator | type | data_size | data  |
 |  2Bytes   | int  | int(Byte) | nBytes|
---------------------------------------
filter先进行separator判断，（separator既是分隔符也是我们的标识符）
不符合规格的数据丢弃
 */

public class FilterHandler extends ChannelInboundHandlerAdapter {

    Logger log = LogManager.getLogger("filter");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        log.warn("filter");
        // Done:怎么过滤信息，一个非用户建立连接，发送消息，我们要过滤出来。但问题是，此前我们用序列化的类封装数据，查看数据内部信息比较麻烦。
        // 这里有两个办法：一个是在bytebuf的序列化数据之前传一个识别符，判断它是我们（已经加入imv3系统）的用户，如用户的uid。
        // 还一个是用protobuf，统一传输数据的格式，让filter在encoder之后进行过滤。
        // 这里我们是通过识别符来判别的

        if (msg instanceof ByteBuf){
            byte[] identifier = new byte[2];
            ((ByteBuf) msg).readBytes(identifier);
            ((ByteBuf) msg).readerIndex(0);
            if(!Comparator.compare(identifier, IMConfiguration.SEPARATOR)){
                log.info("非IM消息,使用html解析");
                ctx.pipeline()
                        .addAfter("FilterHandler", "HttpServerCodec", new HttpServerCodec())
                        .addAfter("HttpServerCodec", "ChunkedWriteHandler", new ChunkedWriteHandler())
                        .addAfter("ChunkedWriteHandler", "HttpObjectAggregator", new HttpObjectAggregator(1024*64))
                        .addAfter("HttpObjectAggregator", "HttpRequestHandler", new HttpRequestHandler());
                ctx.fireChannelRead(msg);
            }else{
                ctx.fireChannelRead(msg);
            }
        }else{
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress()+"建立连接");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress()+"断开连接");
        super.channelInactive(ctx);
    }

//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        log.info("handler added");
//    }
//
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        log.info("handler removed");
//
//    }
}
