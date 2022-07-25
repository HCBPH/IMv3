package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.*;
import com.ycj.imv3.mapper.GroupMapper;
import com.ycj.imv3.mapper.GroupMembersMapper;
import com.ycj.imv3.mapper.HistoryMapper;
import com.ycj.imv3.mapper.UserMapper;
import com.ycj.imv3.starter.IMServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author 53059
 * @date 2022/1/14 15:45
 */
public class LoginHandler extends SimpleChannelInboundHandler<LoginOuterClass.Login>{

    Logger log = LogManager.getLogger("login");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginOuterClass.Login msg) throws Exception {
//        long t1 = System.currentTimeMillis();
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                long uid = msg.getUser().getUid();
                String password = msg.getUser().getPassword();
                String username = msg.getUser().getUsername();
                try(SqlSession session = IMServer.getInstance().getSessionFactory().openSession()) {
                    final UserMapper mapper = session.getMapper(UserMapper.class);
                    final User user = mapper.getUserByUid(uid);
                    final ResponseOuterClass.Response.Builder builder = ResponseOuterClass.Response.newBuilder()
                            .setFrom(IMConfiguration.UID_SERVER)
                            .setTo(uid)
                            .setTimestamp(System.currentTimeMillis());
                    if (user == null){
                        builder.setType(IMConfiguration.RESPONSE_TYPE_LOGIN_FAIL)
                                        .setContent("用户不存在");
                        ResponseOuterClass.Response res = builder.build();
                        ctx.channel().writeAndFlush(res);
                        log.info(uid+":"+username+"用户不存在");
                    }else{
                        if (user.getPassword().equals(password)){
                            Channel t_ch = IMServer.getInstance().getUsers().get(uid);
                            if(t_ch != null){
                                log.warn(username+"("+uid+")"+"被强占下线");
                                t_ch.closeFuture();
                            }
                            // 将连接保存到内存，即用户在线
                            IMServer.getInstance().getUsers().put(uid, ctx.channel());
                            builder.setType(IMConfiguration.RESPONSE_TYPE_LOGIN_SUCCESS)
                                    .setContent("登录成功");
                            ResponseOuterClass.Response res = builder.build();
                            ctx.channel().writeAndFlush(res);
                            log.info(user.getUsername()+"("+uid+")"+"登录IMv3");
                            // 加入组
                            final GroupMembersMapper membersMapper = session.getMapper(GroupMembersMapper.class);
                            final Hashtable<Long, Group> groups = IMServer.getInstance().getGroups();
                            membersMapper.findGroupsByUID(uid).forEach(gid->groups.get(gid).add(uid, ctx.channel(), user.getUsername()));

                            // 发送未读消息
                            final HistoryMapper history = session.getMapper(HistoryMapper.class);
                            history.findUnreadSingle(uid).forEach(ctx::writeAndFlush);
                            history.findUnreadGroup(uid).forEach(ctx::writeAndFlush);
                            history.deleteSingleUnread(uid);
                            history.deleteGroupUnread(uid);
                            session.commit();
                        }else{
                            builder.setType(IMConfiguration.RESPONSE_TYPE_LOGIN_FAIL)
                                    .setContent("密码错误");
                            ResponseOuterClass.Response res = builder.build();
                            ctx.channel().writeAndFlush(res);
                            log.info(uid+":"+username+"登录密码错误");
                        }
                    }
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        });
//        long t2 = System.currentTimeMillis();
//        log.info("处理时长:"+(t2-t1)+"ms");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Hashtable<Long, Channel> users = IMServer.getInstance().getUsers();
        Long uid = null;
        for(Map.Entry<Long, Channel> entry:users.entrySet()){
            if (entry.getValue() == ctx.channel()){
                    uid = entry.getKey();
            }
        }
        if (uid == null){
            log.debug("列表中不存在该用户 "+uid);
        }else{
            users.remove(uid);
            log.info("移除用户 "+uid);
        }
        super.channelInactive(ctx);
    }
}
