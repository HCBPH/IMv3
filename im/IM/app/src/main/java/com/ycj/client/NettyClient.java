package com.ycj.client;

import android.util.Log;

import com.ycj.common.Common;
import com.ycj.entity.Group;
import com.ycj.entity.Message;
import com.ycj.entity.Request;
import com.ycj.handler.*;
import com.ycj.listener.NettyListener;

import org.xml.sax.HandlerBase;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 53059
 * @date 2021/7/12 17:58
 */
public class NettyClient {
    private EventLoopGroup eventGroup;
    private boolean isStart = false;
    private static NettyClient instance;
    private Channel channel;
    private static String UID;
    private static NettyListener listener;

    private ResponseHandler responseHandler;
    private MessageHandler messageHandler;

    public ArrayList<Group> groups;

    public ArrayList<Group> getGroups(){
        if (groups == null){
            synchronized (NettyClient.class){
                groups = new ArrayList<>();
                groups.add(new Group("0"));
            }
        }
        return groups;
    }

    public static NettyClient getInstance(String uid, NettyListener listener) {
        if (instance == null) {
            synchronized (NettyClient.class) {
                instance = new NettyClient();
                NettyClient.listener = listener;
                UID = uid;
            }
        }
        return instance;
    }

    public static NettyClient getInstance() {
        return instance;
    }

    private NettyClient() {
    }

    public void setListener(NettyListener listener){
        NettyClient.listener = listener;
        responseHandler.setListener(listener);
        messageHandler.setListener(listener);
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                eventGroup = new NioEventLoopGroup();
                try {
                    responseHandler = new ResponseHandler(listener);
                    messageHandler = new MessageHandler(listener);
                    Bootstrap b = new Bootstrap();
                    b.group(eventGroup)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.SO_REUSEADDR, true)
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline()
                                            .addLast(new IdleStateHandler(0, Common.IDL_WRITE_TIME, 0, TimeUnit.SECONDS))
                                            .addLast(new NettyDecoder())
                                            .addLast(new NettyEncoder())
                                            .addLast(new HeartbeatHandler())
                                            .addLast(responseHandler)
                                            .addLast(messageHandler);
                                }
                            });
                    ChannelFuture f = b.connect(Common.SERVER_ADDR, Common.SERVER_PORT).sync();
                    channel = f.channel();
                    channel.writeAndFlush(new Request(1, "0", UID, ""));
                    isStart = true;
                    f.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    isStart = false;
                    NettyClient.this.stop();
                }
            }
        }.start();
    }

    public static String getUID() {
        return UID;
    }

    public boolean isStart(){
        return isStart;
    }

    public void stop() {
        isStart = false;
        eventGroup.shutdownGracefully();
        Log.d("NettyClient","服务器关闭");
    }

    // 创建分组
    public void createGroup(String gid){
        channel.writeAndFlush(new Request(0, gid, UID, "")).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Log.d("NettyClient","申请创建组"+gid);
            }
        });
    }

    // 加入分组
    public void joinGroup(String gid){
        channel.writeAndFlush(new Request(1, gid, UID, "")).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Log.d("NettyClient","申请加入组"+gid);
            }
        });
    }

    // 退出分组
    public void quitGroup(String gid){
        channel.writeAndFlush(new Request(2, gid, UID, "")).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Log.d("NettyClient","申请退出组"+gid);
            }
        });
    }

    // 组发送消息
    public void sendGroup(String gid, String data){
        channel.writeAndFlush(new Message(1, 0, UID, gid, UID, System.currentTimeMillis(), data)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Log.d("NettyClient","发送成功");
            }
        });
    }

    // 获取用户状态
//    public Map<String, Integer> getUserState(ArrayList<String> userList){
//        channel.writeAndFlush(new Request(3, UID, userList,"")).addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                Log.d("NettyClient","获取用户状态");
//            }
//        });
//        return
//    }

    public void sendMsg(String dst, String data){
        channel.writeAndFlush(new Message(0, 0, UID, dst, UID, System.currentTimeMillis(), data)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Log.d("NettyClient","发送成功");
            }
        });
    }


}
