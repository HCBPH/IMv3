package com.ycj.imv3.initializer;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author 53059
 * @date 2022/1/19 0:28
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new IdleStateHandler(0, IMConfiguration.HEARTBEAT_TIME_WRITE, 0, TimeUnit.SECONDS))
                .addLast(new ClientDecoder())
                .addLast(new Encoder())
                .addLast(new ClientHeartbeatHandler())
                .addLast(new ClientMessageHandler());
    }
}
