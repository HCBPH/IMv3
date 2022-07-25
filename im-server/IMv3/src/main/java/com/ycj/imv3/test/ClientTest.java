package com.ycj.imv3.test;

import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.GroupMessageOuterClass;
import com.ycj.imv3.entity.Location;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.entity.ResponseOuterClass;
import com.ycj.imv3.listener.*;
import com.ycj.imv3.mapper.TestMapper;
import com.ycj.imv3.starter.IMClient;
import com.ycj.imv3.starter.IMServer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * @author 53059
 * @date 2022/1/19 0:54
 */
public class ClientTest {
    public static void main(String[] args) throws InterruptedException {
        // todo:一、获取IMClient daemon
        IMClient client = IMClient.getInstance();

        // todo:二、daemon的启动关闭监听
        client.setBootstrapListener(new BootstrapListener() {
            @Override
            public void afterBoot(boolean isSuccess) {
                if(isSuccess){
                    System.out.println("BootstrapListener>>> imv3 daemon start!");
                }else{
                    System.out.println("BootstrapListener>>> failed starting imv3 daemon!");
                }
            }

            @Override
            public void afterStop() {
                System.out.println("BootstrapListener>>> imv3 daemon stop!");
            }

            @Override
            public void stateChange(String s) {

            }
        });

        // todo:三、监听收到消息 （有Message和GroupMessage）
        client.setMessageListener(new MessageListener() {
            @Override
            public void receiveMessage(MessageOuterClass.Message msg) {
                System.out.println("MessageListener>>> "+msg.getFrom() + " --> " + msg.getTo() + "[" + new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date(msg.getTimestamp())) + "]: " + msg.getContent());
            }

            @Override
            public void receiveGroupMessage(GroupMessageOuterClass.GroupMessage msg) {
                System.out.println("MessageListener>>> "+msg.getFrom() + " --> " + msg.getTo() + "[" + new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(new Date(msg.getTimestamp())) + "]: " + msg.getContent());
            }
        });

        // todo:四、监听服务端的返回，包括消息是否发送成功，登录是否成功等等，一般发送一个消息给服务器，会返回一个response
        client.setResponseListener(new ResponseListener() {
            @Override
            public void receiveResponse(ResponseOuterClass.Response response) {
                System.out.println("ResponseListener>>> ("+response.getType()+")"+response.getContent());
            }
        });

        // todo:五、Error暂时还不需要实现，设想是 客户端的IM daemon发生错误，通过这个接口反馈给Android
        client.setHeartbeatListener(new HeartbeatListener() {
            @Override
            public Location sendHeartbeat() {
                return new Location(31.222f, 44.2222f);
            }

            @Override
            public void receiveHeartbeat() {

            }
        });

        // todo:六、Error暂时还不需要实现，设想是 客户端的IM daemon发生错误，通过这个接口反馈给Android
        client.setErrorListener(new ErrorListener() {
            @Override
            public void receiveError(String err) {

            }
        });



        // todo:七、一定要开一个线程，应为daemon是阻塞的
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // todo:八、传入当前用户的用户名，uid，密码（需要通过密码验证），这里用户名可以随便填写，uid是关键，要和USER表中的uid对应
                    client.run("computer", 1L, "admin");
//                    client.run("mobile", 2L, "admin");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();


        /*
        * todo:九、可以主动调用的API，根据需求自定义嵌入到具体的应用当中
        *   1.IMClient.getInstance.sendMsg(long to, String s)                        传入目标uid和发送的文字
        *   2.IMClient.getInstance.sendGroupMsg(long gid, String s)                  发送群消息
        *   3.IMClient.getInstance.login(long uid, String password, String uname)    登录IMserver，注意这里一定要login，因为服务器只认登录的用户的消息
        *                                                       默认IMClient.run()方法中就调用了login
        *                                                       如果登录失败就再次调用run或者login
        *                                                       最好调用login，因为run调用过之后，如果和Imserver没断链，就不会被调用
        *   下面的API是通过http实现的，get/post都可以，最好用get，post的参数提交方式太多，我不能保证读取所有格式
        *   4.createGroup           创建群聊，传群聊名称和创建人id，返回gid
        *   5.joinGroup             加入组，传群聊gid和uid，state 0失败，1成功
        *   6.leaveGroup            同上
        *   7.dropGroup             传gid
        *   8.getGroupInfo          同上
        *   9.getOnlineMembers      同上
        *   10.getAllGroupInfo      什么都不用，返回Group类对应的json
        * */

        /**test-start*/
        try(SqlSession sqlSession = IMServer.getInstance().getSessionFactory().openSession()){
            TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
            System.out.println("-------------------------------------------------------------------------------------");
            testMapper.getAll().forEach(System.out::println);
            System.out.println("-------------------------------------------------------------------------------------");
            testMapper.insertOne();
            sqlSession.commit();
            testMapper.getAll().forEach(System.out::println);
            System.out.println("-------------------------------------------------------------------------------------");
        }catch (Exception e){
            e.printStackTrace();
        }
        /**test-end*/


        Scanner scanner = new Scanner(System.in);
        String s;
        Thread.sleep(6000);
//        client.sendMsg(2, "上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放上的家乐福空手道解放");
        while (scanner.hasNext()){
            System.out.print("[client]:");
            s = scanner.nextLine();
//            client.sendMsg(2, s);
            client.sendGroupMsg(962576593, s);
        }
    }
}
