package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.*;
import com.ycj.imv3.mapper.GroupMembersMapper;
import com.ycj.imv3.mapper.HistoryMapper;
import com.ycj.imv3.mapper.UserMapper;
import com.ycj.imv3.starter.IMServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 53059
 * @date 2022/1/31 14:27
 */
public class ServerGroupMessageHandler extends GroupMessageHandler{
    ResponseOuterClass.Response res;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageOuterClass.GroupMessage msg) throws Exception {
        super.channelRead0(ctx, msg);
        dstGroup = IMServer.getInstance().getGroups().get(to);
        log.info(from + " --> group:" + to + "[" + new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date(timestamp)) + "]: " + content);
        if(dstGroup == null){
            log.warn("from:"+from+"-->to:"+to+",group不存在");
            res = ResponseOuterClass.Response.newBuilder()
                    .setTo(from)
                    .setType(IMConfiguration.RESPONSE_TYPE_GROUP_NOT_FOUND)
                    .setContent("组不存在")
                    .setTimestamp(System.currentTimeMillis())
                    .build();
            ctx.writeAndFlush(res);
        }else{
            res = ResponseOuterClass.Response.newBuilder()
                    .setTo(from)
                    .setType(IMConfiguration.RESPONSE_TYPE_MSG_SUCCESS)
                    .setContent("发送成功")
                    .setTimestamp(System.currentTimeMillis())
                    .build();
            ctx.writeAndFlush(res);
            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    final Hashtable<Long, Channel> channels = dstGroup.getChannels();
                    try(final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()) {
                        final HistoryMapper mapper = session.getMapper(HistoryMapper.class);
                        mapper.insertGroupHistory(msg.getFrom(),msg.getTo(),msg.getContent(), msg.getTimestamp());

                        // 遍历group,向所有成员发送信息。
                        final GroupMembersMapper memberMapper = session.getMapper(GroupMembersMapper.class);
                        final ArrayList<User> users = memberMapper.findUsersByGID(msg.getTo());
                        for (User user : users) {
                            final Channel channel = channels.get(user.getUid());
                            // 如果用户不在线
                            if (channel == null){
                                mapper.insertGroupUnread(msg.getFrom(), user.getUid(), msg.getTo(), msg.getContent(), msg.getTimestamp());
                            }else{ // 如果用户在线
                                channel.writeAndFlush(msg);
                            }
                        }
                        session.commit();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            });
        }
    }
}
