package com.ycj.imv3.handler;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.entity.ResponseOuterClass;
import com.ycj.imv3.mapper.HistoryMapper;
import com.ycj.imv3.mapper.UserMapper;
import com.ycj.imv3.starter.IMServer;
import io.netty.channel.ChannelHandlerContext;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 53059
 * @date 2022/1/25 14:43
 */
public class ServerMessageHandler extends MessageHandler{

    ResponseOuterClass.Response res;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageOuterClass.Message msg) throws Exception {
        super.channelRead0(ctx, msg);
        dstChannel = IMServer.getInstance().getUsers().get(to);
        log.info(from + " --> " + to + "[" + new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date(timestamp)) + "]: " + content);
        if(dstChannel == null){
            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    try(SqlSession session = IMServer.getInstance().getSessionFactory().openSession()) {
                        final UserMapper mapper = session.getMapper(UserMapper.class);
                        if(mapper.getUserByUid(msg.getTo())==null){
                            log.warn("from:"+from+"-->to:"+to+",用户不存在");
                            res = ResponseOuterClass.Response.newBuilder()
                                    .setFrom(IMConfiguration.UID_SERVER)
                                    .setTo(from)
                                    .setType(IMConfiguration.RESPONSE_TYPE_DST_NOT_FOUND)
                                    .setContent("用户不存在")
                                    .setTimestamp(System.currentTimeMillis())
                                    .build();
                        }else{
                            log.warn("from:"+from+"-->to:"+to+",用户离线，缓存记录");
                            res = ResponseOuterClass.Response.newBuilder()
                                    .setFrom(IMConfiguration.UID_SERVER)
                                    .setTo(from)
                                    .setType(IMConfiguration.RESPONSE_TYPE_DST_NOT_ONLINE)
                                    .setContent("用户离线")
                                    .setTimestamp(System.currentTimeMillis())
                                    .build();
                            final HistoryMapper history = session.getMapper(HistoryMapper.class);
                            history.insertSingleHistory(msg.getFrom(),msg.getTo(),msg.getContent(), msg.getTimestamp());
                            history.insertSingleUnread(msg.getFrom(),msg.getTo(),msg.getContent(), msg.getTimestamp());
                            session.commit();
                        }
                        ctx.writeAndFlush(res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            dstChannel.writeAndFlush(msg);
            res = ResponseOuterClass.Response.newBuilder()
                    .setFrom(IMConfiguration.UID_SERVER)
                    .setTo(from)
                    .setType(IMConfiguration.RESPONSE_TYPE_MSG_SUCCESS)
                    .setContent("发送成功")
                    .setTimestamp(System.currentTimeMillis())
                    .build();
            ctx.writeAndFlush(res);
            ctx.channel().eventLoop().execute(() -> {
                try(final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()) {
                    final HistoryMapper mapper = session.getMapper(HistoryMapper.class);
                    mapper.insertSingleHistory(msg.getFrom(),msg.getTo(),msg.getContent(), msg.getTimestamp());
                    session.commit();
                } catch (IOException e) {
                    log.error("历史记录缓存错误:",e.getMessage());
                }
            });
        }

    }
}
