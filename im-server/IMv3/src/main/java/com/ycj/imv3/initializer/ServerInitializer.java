package com.ycj.imv3.initializer;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author 53059
 * @date 2022/1/12 17:51
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast("FilterHandler", new FilterHandler())
                .addLast(new IdleStateHandler( IMConfiguration.HEARTBEAT_TIME_READ, 0, 0, TimeUnit.SECONDS))
//                .addLast(new HttpServerCodec())
//                .addLast(new ChunkedWriteHandler())
//                .addLast(new HttpObjectAggregator(1024*64)) // Codec只能解析url中的参数，对于post的body需要aggregator进行解析。
//                .addLast(new HttpRequestHandler())
                .addLast("Encoder", new Encoder())  // 因为Encoder处理的都是IM自定义的类，不处理ByteBuf，所以要注意解码的顺序
                .addLast("ServerDecoder", new ServerDecoder())

                .addLast(new ServerHeartbeatHandler())
                .addLast("LoginHandler", new LoginHandler())
                .addLast("ServerMessageHandler", new ServerMessageHandler())
                .addLast("ServerGroupMessageHandler", new ServerGroupMessageHandler());
    }
}
