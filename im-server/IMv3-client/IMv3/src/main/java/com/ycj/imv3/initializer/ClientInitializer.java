package com.ycj.imv3.initializer;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.handler.*;
import com.ycj.imv3.listener.BootstrapListener;
import com.ycj.imv3.listener.HeartbeatListener;
import com.ycj.imv3.listener.MessageListener;
import com.ycj.imv3.listener.ResponseListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author 53059
 * @date 2022/1/19 0:28
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    MessageListener messageListener;
    ResponseListener responseListener;
    HeartbeatListener heartbeatListener;

    public ClientInitializer(MessageListener l1, ResponseListener l2, HeartbeatListener l3){
        this.messageListener = l1;
        this.responseListener = l2;
        this.heartbeatListener = l3;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new IdleStateHandler(0, IMConfiguration.HEARTBEAT_TIME_WRITE, 0, TimeUnit.SECONDS))
                .addLast(new Encoder())
                .addLast(new ClientDecoder())
                .addLast(new ClientHeartbeatHandler(this.heartbeatListener))
                .addLast(new ResponseHandler(responseListener))
                .addLast(new ClientMessageHandler(messageListener))
                .addLast(new ClientGroupMessageHandler(messageListener));
    }
}
