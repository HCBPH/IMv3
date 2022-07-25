package com.ycj.imv3.starter;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.Group;
import com.ycj.imv3.initializer.ServerInitializer;
import com.ycj.imv3.mapper.GroupMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author 53059
 * @date 2022/1/12 15:19
 */
public class IMServer {

    private Logger log = LogManager.getLogger();

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;
    private static IMServer instance;
    private SqlSessionFactory sessionFactory;
    private Hashtable<Long, Channel> users;
    private Hashtable<Long, Group> groups;

    private IMServer(){}

    public static IMServer getInstance() {
        if(instance == null){
            synchronized (IMServer.class){
                instance = new IMServer();
            }
        }
        return instance;
    }

    public Hashtable<Long, Channel> getUsers() {
        if (users == null){
            synchronized (IMServer.class){
                users = new Hashtable<>();
            }
        }
        return users;
    }

    public Hashtable<Long, Group> getGroups(){
        if (groups == null){
            synchronized (IMServer.class){
                groups = new Hashtable<>();
            }
        }
        return groups;
    }

    public SqlSessionFactory getSessionFactory() throws IOException {
        if(sessionFactory == null){
            synchronized (IMServer.class){
                try (final InputStream inputStream = IMServer.class.getResourceAsStream("/mybatis.xml")) {
                    sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return sessionFactory;
    }

    public void run() {
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup(8);
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.channel(NioServerSocketChannel.class)
                    .group(bossGroup, workGroup)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .localAddress(IMConfiguration.PORT_SERVER)
                    .childHandler(new ServerInitializer())
                    .childOption(ChannelOption.TCP_NODELAY, true);

            ChannelFuture cf = bootstrap.bind().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    log.info("IMv3启动,监听地址:"+ IMConfiguration.ADDR_SERVER+":"+ IMConfiguration.PORT_SERVER);
                    try(final SqlSession session = getSessionFactory().openSession()){
                        final GroupMapper mapper = session.getMapper(GroupMapper.class);
                        final Hashtable<Long, Group> groups = getGroups();
                        mapper.findAllGroups().forEach(x->groups.put(x.getId(), x));
                    }
                }
            }).sync();
            cf.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    log.info("IMv3关闭");
                }
            }).sync();
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    public void stop(){
        if (bossGroup == null && workGroup == null){
            return;
        }
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    public void createGroup(Long creator_uid, String gname){
        Long gid = System.currentTimeMillis() % IMConfiguration.GID_DIGITAL_NUMBER;
        this.createGroup(gid, creator_uid, gname);
    }

    public void createGroup(Long gid, Long creator_uid, String gname){
        final Hashtable<Long, Group> groups = getGroups();
        if(groups.containsKey(gid)){
            log.warn("group:'"+gid+"' has already existed,can't duplicate creation.");
        }else{
            // 在内存中创建组，并在数据库中，写入相应数据
            // groups在初始化的时候，需要读取数据库中的的组信息，尽管服务器开始的时候，里面没有任何成员，但是需要保证，一旦有成员上线，就能将其对应的channel加入到group中。
        }
    }

    public void dropGroup(){}

    public void joinGroup(){}

    public void leaveGroup(){}

}
