package com.ycj.imv3.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.Group;
import com.ycj.imv3.entity.User;
import com.ycj.imv3.mapper.GroupMapper;
import com.ycj.imv3.mapper.GroupMembersMapper;
import com.ycj.imv3.mapper.UserMapper;
import com.ycj.imv3.starter.IMServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.handler.stream.ChunkedNioStream;
import jdk.nashorn.internal.ir.ObjectNode;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author 53059
 * @date 2022/2/6 13:56
 */

// 因为上一个handler是HttpObjectAggregator，传递出来的对象都是FullHttpRequest,所以我们写这个handler来处理request。
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    Logger log = LogManager.getLogger();
    HashMap<String, String> params = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        String body = request.content().toString(StandardCharsets.UTF_8);

        final boolean isKeepAlive = request.headers().get(HttpHeaderNames.CONNECTION) == null;
        String uri = request.uri();
        log.info(uri);
        uri = parseUri(uri);
        final HttpVersion version = request.protocolVersion();
        String finalUri = uri;

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                Map<String, String> res;
                switch (finalUri){
                    case "/index":
                        retFile(ctx, version, isKeepAlive,
                                getClass().getResource("/").getPath()+"\\http\\index.html", "text/html;charset=UTF-8");
                        break;
                    case "/favicon.ico":
                        retFile(ctx, request.protocolVersion(), isKeepAlive,
                                getClass().getResource("/").getPath() + "\\img\\icon.png", "image/png");
                        break;
                    case "/group/create":
                        String name = params.get("gname");
                        String creator_id = params.get("creator_id");
                        res = new HashMap<>();

                        // 读取body，如果body不为空，覆盖name、creator_id
                        if (!body.trim().equals("")){
                            HashMap<String, String> bodyParams = parseBody(body);
                            if (bodyParams != null){
                                if (bodyParams.size()==2){
                                    name = bodyParams.get("gname");
                                    creator_id = bodyParams.get("creator_id");
                                }
                            }
                        }

                        if(name != null && creator_id != null){
                            long gid = System.currentTimeMillis() % IMConfiguration.GID_DIGITAL_NUMBER;
                            try (final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()){
                                final GroupMapper mapper = session.getMapper(GroupMapper.class);
                                if( mapper.createGroup(gid, name, Long.parseLong(creator_id)) == 1){
                                    final Group group = new Group(gid);
                                    final Channel createor_channel = IMServer.getInstance().getUsers().get(Long.parseLong(creator_id));
                                    if (createor_channel != null){
                                        group.add(Long.parseLong(creator_id), createor_channel);
                                    }
                                    IMServer.getInstance().getGroups().put(gid, group);
                                    final GroupMembersMapper membersMapper = session.getMapper(GroupMembersMapper.class);
                                    membersMapper.join(gid, Long.parseLong(creator_id));
                                    session.commit();
                                    res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_SUCCESS);
                                    res.put(IMConfiguration.RESULT_KEY_GID, String.valueOf(gid));
                                }else{
                                    res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                    res.put(IMConfiguration.RESULT_KEY_REASON, "unknown reason");
                                }
                            }catch (Exception e){
                                log.error(e.getMessage());
                                res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                res.put(IMConfiguration.RESULT_KEY_REASON, "db error");
                            }
                        }else{
                            res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                            res.put(IMConfiguration.RESULT_KEY_REASON, "error parsing params");
                        }
                        retJSON(ctx, version, isKeepAlive, res);
                        break;
                    case "/group/drop":
                        String gid = params.get("gid");
                        res = new HashMap<>();
                        if(gid != null){
                            try (final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()){
                                final GroupMapper mapper = session.getMapper(GroupMapper.class);
                                if (!checkGroup(Long.parseLong(gid), mapper)){
                                    res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                    res.put(IMConfiguration.RESULT_KEY_REASON, "group doesn't exist");
                                }else{
                                    if(mapper.dropGroup(Long.parseLong(gid)) == 1){
                                        final GroupMembersMapper membersMapper = session.getMapper(GroupMembersMapper.class);
                                        membersMapper.dropOneGroup(Long.parseLong(gid));
                                        session.commit();
                                        res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_SUCCESS);
                                    }else{
                                        res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                        res.put(IMConfiguration.RESULT_KEY_REASON, "unknown reason");
                                    }
                                }
                            }catch (Exception e){
                                log.error(e.getMessage());
                                res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                res.put(IMConfiguration.RESULT_KEY_REASON, "db error");
                            }
                        }else{
                            res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                            res.put(IMConfiguration.RESULT_KEY_REASON, "error parsing params");
                        }
                        retJSON(ctx, version, isKeepAlive, res);
                        break;
                    case "/group/add":
                        String uid = params.get("uid");
                        gid = params.get("gid");
                        res = new HashMap<>();
                        if(gid != null){
                            try (final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()){
                                final GroupMembersMapper mapper = session.getMapper(GroupMembersMapper.class);
                                if (checkGroup(Long.parseLong(gid))){
                                    if (checkUser(Long.parseLong(gid), Long.parseLong(uid), mapper)){
                                        res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                        res.put(IMConfiguration.RESULT_KEY_REASON, "user has already joined the group");
                                    }else{
                                        if(mapper.join(Long.parseLong(gid), Long.parseLong(uid)) == 1){
                                            session.commit();
                                            res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_SUCCESS);
                                        }else{
                                            res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                            res.put(IMConfiguration.RESULT_KEY_REASON, "unknown reason");
                                        }
                                    }
                                }else{
                                    res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                    res.put(IMConfiguration.RESULT_KEY_REASON, "group doesn't exist");
                                }
                            }catch (Exception e){
                                log.error(e.getMessage());
                                res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                res.put(IMConfiguration.RESULT_KEY_REASON, "db error");
                            }
                        }else{
                            res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                            res.put(IMConfiguration.RESULT_KEY_REASON, "error parsing params");
                        }
                        retJSON(ctx, version, isKeepAlive, res);
                        break;
                    case "/group/remove":
                        uid = params.get("uid");
                        gid = params.get("gid");
                        res = new HashMap<>();
                        if(gid != null){
                            try (final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()){
                                final GroupMembersMapper mapper = session.getMapper(GroupMembersMapper.class);


                                if (!checkUser(Long.parseLong(gid), Long.parseLong(uid), mapper)){
                                    // 用户不在组中
                                    res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                    res.put(IMConfiguration.RESULT_KEY_REASON, "user doesn't exist in the group");
                                }else{
                                    // 不能使用remove，因为gid，uid，state组成主键，一个user退出，state设为0，当他加入再退出，（gid，uid，0）已经存在
                                    if(mapper.drop(Long.parseLong(gid), Long.parseLong(uid)) == 1){
                                        session.commit();
                                        res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_SUCCESS);
                                    }else{
                                        res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                        res.put(IMConfiguration.RESULT_KEY_REASON, "unknown error");
                                    }
                                }
                            }catch (Exception e){
                                log.error(e.getMessage());
                                res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                                res.put(IMConfiguration.RESULT_KEY_REASON, "db error");
                            }
                        }else{
                            res.put(IMConfiguration.RESULT_KEY_STATE, IMConfiguration.RESULT_VALUE_FAIL);
                            res.put(IMConfiguration.RESULT_KEY_REASON, "error parsing params");
                        }
                        retJSON(ctx, version, isKeepAlive, res);
                        break;
                    case "/group/allgroupinfo":
                        try (final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()){
                            final GroupMapper mapper = session.getMapper(GroupMapper.class);
                            final ArrayList<Group> allGroups = mapper.findAllGroups();
                            retJSON(ctx, version, isKeepAlive, allGroups);
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                        break;
                    case "/group/info":
                        gid = params.get("gid");
                        try (final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()){
                            final GroupMapper mapper = session.getMapper(GroupMapper.class);
                            final GroupMembersMapper membersMapper = session.getMapper(GroupMembersMapper.class);
                            final Group group = mapper.findGroup(Long.parseLong(gid));
                            ArrayList<User> users = membersMapper.findUsersByGID(Long.parseLong(gid));
                            group.setUsers(users);
                            retJSON(ctx, version, isKeepAlive, group);
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                        break;
                    case "/group/online":
                        gid = params.get("gid");
                        HashMap<Long, String> onlineMembers = null;
                        Group group = IMServer.getInstance().getGroups().get(Long.parseLong(gid));
                        if(group.getUsers() != null){
                            onlineMembers = new HashMap<>();
                            for (User u : group.getUsers()) {
                                onlineMembers.put(u.getUid(), u.getUsername());
                            }
                        }
                        retJSON(ctx, version, isKeepAlive, onlineMembers);
                        break;
                    case "/user/online":
                        final Enumeration<Long> onlineUsers_t = IMServer.getInstance().getUsers().keys();
                        HashMap<Long, String> onlineUsers = new HashMap<>();
                        try (final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()){
                            final UserMapper mapper = session.getMapper(UserMapper.class);
                            while (onlineUsers_t.hasMoreElements()){
                                Long u = onlineUsers_t.nextElement();
                                final User user = mapper.getUserByUid(u);
                                onlineUsers.put(user.getUid(), user.getUsername());
                            }
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                        retJSON(ctx, version, isKeepAlive, onlineUsers);
                        break;
                    case "/user/groups":
                        HashMap<Long, String> usersGroup = new HashMap<>();
                        uid = params.get("uid");
                        try (final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()){
                            final GroupMembersMapper mapper = session.getMapper(GroupMembersMapper.class);
                            ArrayList<Group> fullGroupsByUID = mapper.findFullGroupsByUID(Integer.parseInt(uid));
                            retJSON(ctx, version, isKeepAlive, fullGroupsByUID);
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                        break;
                    default:
                        log.info("意外请求:"+finalUri);
                        retFile(ctx, version, isKeepAlive,
                                getClass().getResource("/").getPath()+"\\http\\index.html", "text/html;charset=UTF-8");
                        break;
                }
            }
        });



    }


//    private getPostRequest(HttpRequest request, ){
//        if (request.method() != HttpMethod.POST){
//            retFile(ctx, request.protocolVersion(), isKeepAlive,
//                    getClass().getResource("/").getPath()+"\\http\\error.html", "text/html;charset=UTF-8");
//            return;
//        }
//final HttpPostRequestDecoder postRequest = new HttpPostRequestDecoder(request);
//        System.out.println(((MixedAttribute)postRequest.getBodyHttpData("name")).getValue());
//    final byte[] buf = new byte[request.content().readableBytes()];
//        request.content().getBytes(request.content().readerIndex(), buf);
//    final Map map = new ObjectMapper().readValue(buf, Map.class);
//        log.info("json:"+new String(buf));
//        log.info("json:"+map.get("name"));
//    }


    private HashMap<String, String> parseBody(String stringBody){
        HashMap<String, String> res = new HashMap<>();
        String key = "";
        String value = "";
        boolean flag = false;
        String[] split = stringBody.split("[\n|\r\n]");
        for (String s : split) {
            if (flag) {
                if (!s.trim().equals("")) {
                    value = strip(s.trim(),"\"");
                    res.put(key, value);
                    flag = false;
                }
                continue;
            }
            if (s.contains("form-data")) {
                String[] formSplit = s.split(";");
                String[] kvSplit = formSplit[formSplit.length - 1].trim().split("=");
                if (kvSplit.length != 2) {
                    return null;
                }
                if (!kvSplit[0].trim().equals("name")) {
                    return null;
                }
                key = strip(kvSplit[1],"\"");
                flag = true;
            }
        }
        return res;
    }

    private String strip(String s, String trim){
        if(s == null){
            return null;
        }
        if(trim == null){
            return s.trim();
        }
        if (s.equals("")) {
            return s;
        }
        if (s.length() == 1){
            if(s.equals(trim)){
                return "";
            }else {
                return s;
            }
        }
        if (s.startsWith(trim)){
            s = s.substring(trim.length());
        }
        if (s.endsWith(trim)){
            s = s.substring(0, s.length()-trim.length());
        }
        return s;
    }


    private String parseUri(String uri){

        final String[] split = uri.split("\\?");

        if(split.length == 2){

            uri = split[0];

            final Iterator<String> iterator = Arrays.stream(split[1].split("&")).iterator();
            params.clear();

            while(iterator.hasNext()){
                final String[] param = iterator.next().split("=");
                if (param.length == 2){
                    params.put(param[0], param[1]);
                }else{
                    log.info("uri参数解析错误!");
                }
            }
        }
        return uri;
    }


    private boolean checkGroup(long gid){
        try(final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()) {
            final GroupMapper mapper = session.getMapper(GroupMapper.class);
            return this.checkGroup(gid, mapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkGroup(long gid, GroupMapper mapper){
        final Group find = mapper.findGroup(gid);
        return find != null;
    }


    private boolean checkUser(long gid, long uid){
        try(final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()) {
            final GroupMembersMapper mapper = session.getMapper(GroupMembersMapper.class);
            return this.checkUser(gid, uid, mapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



    private boolean checkUser(long gid, long uid, GroupMembersMapper mapper){
        int t = mapper.check(gid, uid);
        return t == 1;
    }



    protected void retJSON(ChannelHandlerContext ctx, HttpVersion version, boolean isKeepAlive, Object res) {
        try{
            final DefaultHttpResponse response = new DefaultHttpResponse(version, HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            final byte[] json = new ObjectMapper().writeValueAsBytes(res);

            if(isKeepAlive){
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, json.length);
            }

            ctx.write(response);

            ctx.write(Unpooled.buffer(json.length).writeBytes(json));

            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()){
                                log.info("json发送成功");
                            }else{
                                log.info("json发送失败");
                            }
                            if (!isKeepAlive){
                                ctx.close();
                            }
                        }
                    });
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }


    protected void retFile(ChannelHandlerContext ctx, HttpVersion version, boolean isKeepAlive, String localFile, String contentType) {
        final DefaultHttpResponse response = new DefaultHttpResponse(version, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);

        try{
            final RandomAccessFile file = new RandomAccessFile(localFile, "r");

            if(isKeepAlive){
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
            }

            ctx.write(response);
//        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            ctx.write(new ChunkedNioFile(file.getChannel()));
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()){
                                log.info("file发送成功");
                            }else{
                                log.info("file发送失败");
                            }
                            if (!isKeepAlive){
                                ctx.close();
                            }
                        }
                    });
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }
}
