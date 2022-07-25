package com.ycj.imv3.starter;

import android.util.Log;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.LoginOuterClass;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.entity.UserOuterClass;
import com.ycj.imv3.initializer.ClientInitializer;
import com.ycj.imv3.listener.IMClientListener;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author 53059
 * @date 2022/1/12 15:18
 */
public class IMClient {

    private static IMClient instance;
    private NioEventLoopGroup channelGroup;
    private Channel channel = null;
    private static boolean isRunning = false;

    private IMClientListener listener;

//    private long uid = 5;
//    private String password = "123456";
//    private String username = "test";
//    private String serverAddr = "localhost";
//    private int serverPort = 29233;

    private IMClient(){}

    public static IMClient getInstance(){
        synchronized (IMClient.class){
            if(instance == null){
                instance = new IMClient();
            }
        }
        return instance;
    }

    public void run(String uname, long uid, String pwd, IMClientListener listener){
        this.listener = listener;
        if (channelGroup != null){
            if (isRunning || !channelGroup.isShutdown()){
                Log.e("IMClient", "imv3 has already been running, please check first to start.");
                listener.afterBoot(false);
                return;
            }
        }
        IMConfiguration.UID_SELF = uid;
        IMConfiguration.PASSWORD_SELF = pwd;
        IMConfiguration.USERNAME_SELF = uname;
        channelGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.channel(NioSocketChannel.class)
                    .group(channelGroup)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ClientInitializer());
            ChannelFuture cf = bootstrap.connect(IMConfiguration.ADDR_SERVER, IMConfiguration.PORT_SERVER).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()){
                        Log.i("IMClient", "成功连接服务器" + IMConfiguration.ADDR_SERVER + ":" + IMConfiguration.PORT_SERVER);
                        isRunning = true;
                    }else{
                        Log.e("IMClient", "服务器连接失败，请重试!");
                    }
                    listener.afterBoot(channelFuture.isSuccess());
                }
            }).sync();
            channel = cf.channel();
            login(IMConfiguration.UID_SELF, IMConfiguration.PASSWORD_SELF, IMConfiguration.USERNAME_SELF);
            channel.closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Log.i("IMClient", "IMv3关闭");
                    isRunning = false;
                    listener.afterStop(true);
                }
            }).sync();
        }catch (Exception e){
            Log.e("IMClient" ,e.getMessage());
            listener.afterBoot(false);
        }finally {
            stop();
        }
    }

    public IMConfiguration.State state(){
        if (!isRunning&&channelGroup.isShutdown()){
            return IMConfiguration.State.SHUTDOWN;
        }else{
            return IMConfiguration.State.RUNNING;
        }
    }

    public void stop(){
        if (!isRunning && !channelGroup.isShuttingDown()){
            Log.e("IMClient", "imv3 has already been down, please check first to stop.");
            return;
        }
        channelGroup.shutdownGracefully();
        Log.i("IMClient", "IMv3关闭!");
//        listener.afterStop(true);
    }

    public void setListener(IMClientListener listener){
        this.listener = listener;
    }

    public void login(long uid, String password, String uname){
        UserOuterClass.User user = UserOuterClass.User.newBuilder()
                .setUid(uid)
                .setPassword(password)
                .setUsername(uname)
                .build();
        LoginOuterClass.Login login = LoginOuterClass.Login.newBuilder()
                .setUser(user)
                .build();
        channel.writeAndFlush(login).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    Log.i("IMClient", "用户登录成功!");
                }else{
                    Log.i("IMClient", "用户登录失败，请重新尝试!");
                }
            }
        });
    }

    public void sendMsg(long to, String s){
        MessageOuterClass.Message msg = MessageOuterClass.Message.newBuilder()
                .setFrom(IMConfiguration.UID_SELF)
                .setTo(to)
                .setContent(s)
                .setTimestamp(System.currentTimeMillis())
                .build();
//        log.info(msg.toString());
        channel.writeAndFlush(msg);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
