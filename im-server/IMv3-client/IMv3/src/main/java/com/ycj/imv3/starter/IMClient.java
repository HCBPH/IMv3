package com.ycj.imv3.starter;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.GroupMessageOuterClass;
import com.ycj.imv3.entity.LoginOuterClass;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.entity.UserOuterClass;
import com.ycj.imv3.initializer.ClientInitializer;
import com.ycj.imv3.listener.*;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 53059
 * @date 2022/1/12 15:18
 */
public class IMClient {

    private static IMClient instance;
    private NioEventLoopGroup channelGroup;
    private Channel channel = null;
    private static boolean isRunning = false;
    Logger log = LogManager.getLogger();


    private BootstrapListener bootstrapListener;
    private MessageListener messageListener;
    private ResponseListener responseListener;
    private HeartbeatListener heartbeatListener;
    private ErrorListener errorListener;



    private IMClient(){}

    public static IMClient getInstance(){
        synchronized (IMClient.class){
            if(instance == null){
                instance = new IMClient();
            }
        }
        return instance;
    }

    public void run(String uname, long uid, String pwd) throws Exception {
        synchronized (this){

            if(this.bootstrapListener == null || this.messageListener == null || this.responseListener == null
                    || this.heartbeatListener == null){
                throw new Exception("IMClient启动失败，缺少Listener。");
            }

            if (channelGroup != null){
                if (isRunning || !channelGroup.isShutdown()){
                    log.warn("imv3 has already been running, please check first to start.");
                    bootstrapListener.afterBoot(false);
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
                        .handler(new ClientInitializer(this.messageListener, this.responseListener, this.heartbeatListener));
                ChannelFuture cf = bootstrap.connect(IMConfiguration.ADDR_SERVER, IMConfiguration.PORT_SERVER).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()){
                            log.info("成功连接服务器" + IMConfiguration.ADDR_SERVER + ":" + IMConfiguration.PORT_SERVER);
                            isRunning = true;
                        }else{
                            log.error("服务器连接失败，请重试!");
                        }
                        bootstrapListener.afterBoot(channelFuture.isSuccess());
                    }
                }).sync();
                channel = cf.channel();
                login(IMConfiguration.UID_SELF, IMConfiguration.PASSWORD_SELF, IMConfiguration.USERNAME_SELF);
                channel.closeFuture().addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        log.info("IMv3关闭");
                        isRunning = false;
                    }
                }).sync();
            }catch (Exception e){
                log.error(e.getMessage());
            }finally {
                stop();
            }
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
            log.error("imv3 has already been down, please check first to stop.");
            return;
        }
        channelGroup.shutdownGracefully();
        bootstrapListener.afterStop();
    }

    public BootstrapListener getBootstrapListener() {
        return bootstrapListener;
    }

    public void setBootstrapListener(BootstrapListener bootstrapListener) {
        this.bootstrapListener = bootstrapListener;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public ResponseListener getResponseListener() {
        return responseListener;
    }

    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public HeartbeatListener getHeartbeatListener() {
        return heartbeatListener;
    }

    public void setHeartbeatListener(HeartbeatListener heartbeatListener) {
        this.heartbeatListener = heartbeatListener;
    }

    public ErrorListener getErrorListener() {
        return errorListener;
    }

    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
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
                    log.info("login发送!");
                }else{
                    log.info("用户登录失败，请重新尝试!");
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


    public void sendGroupMsg(long gid, String s){
        GroupMessageOuterClass.GroupMessage msg = GroupMessageOuterClass.GroupMessage.newBuilder()
                .setFrom(IMConfiguration.UID_SELF)
                .setTo(gid)
                .setContent(s)
                .setTimestamp(System.currentTimeMillis())
                .build();
        channel.writeAndFlush(msg);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
