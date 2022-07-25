package com.ycj.imv3.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.entity.Group;
import com.ycj.imv3.entity.MessageOuterClass;
import com.ycj.imv3.entity.User;
import com.ycj.imv3.mapper.GroupMapper;
import com.ycj.imv3.mapper.GroupMembersMapper;
import com.ycj.imv3.mapper.HistoryMapper;

import com.ycj.imv3.starter.IMServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author 53059
 * @date 2022/1/14 16:29
 */
public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
//        try (final SqlSession session = IMServer.getInstance().getSessionFactory().openSession()) {
//            final HistoryMapper mapper = session.getMapper(HistoryMapper.class);
//            final ArrayList<MessageOuterClass.Message> res = mapper.findSingleHistory(1, 2, "2022-02-11 23:53:33", "2022-02-11 23:53:36");
//            for (MessageOuterClass.Message m : res) {
//                System.out.println(m.toString());
//            }
//        }
//        // 1.当前类的路径(.class文件的路径)
//        // file:/D:/Project/java_project/IMv3/target/classes/com/ycj/imv3/test/
//        System.out.println(Test.class.getResource(""));
//        // 2.类路径
//        // file:/D:/Project/java_project/IMv3/target/classes/
//        System.out.println(Test.class.getResource("/"));
//        // 3.类加载器的路径(即类路径)
//        // file:/D:/Project/java_project/IMv3/target/classes/
//        System.out.println(Test.class.getClassLoader().getResource(""));
//        // 4.类路径
//        // file:/D:/Project/java_project/IMv3/target/classes/
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
//        // 5.类路径
//        // /D:/Project/java_project/IMv3/target/classes/
//        System.out.println(Test.class.getProtectionDomain().getCodeSource().getLocation().getFile());
//        // 6.工作目录
//        // D:\Project\java_project\IMv3
//        System.out.println(System.getProperty("user.dir"));
//
//        System.out.println(Test.class.getResource("/mybatis.xml"));
//        System.out.println(Test.class.getResource("/img/icon.png").getPath());
//        System.out.println(Test.class.getResource("/img/icon.png").getFile());

//        final Hashtable<Long, Long> table = new Hashtable<>();
//        table.put(1L, 2L);
//        System.out.println(table.containsKey(1L));
//        System.out.println(new Date());

        /**
         * ----------------------------733887376358894554196279
         * Content-Disposition: form-data; name="gname"
         *
         * 测试新
         * ----------------------------733887376358894554196279
         * Content-Disposition: form-data; name="creator_id"
         *
         * 1
         * ----------------------------733887376358894554196279--
         * */

        String s = "----------------------------733887376358894554196279\n" +
                "Content-Disposition: form-data; name=\"gname\"\n" +
                "\n" +
                "测试新\n" +
                "----------------------------733887376358894554196279\n" +
                "Content-Disposition: form-data; name=\"creator_id\"\n" +
                "\n" +
                "1\n" +
                "----------------------------733887376358894554196279--";

        HashMap<String, String> res = new HashMap<>();
        String key = "";
        String value = "";
        boolean flag = false;
        String[] split = s.split("[\n|\r\n]");
        for(int i=0;i<split.length;i++){
            if (flag){
                if (!split[i].trim().equals("")){
                    value = split[i].trim();
                    res.put(key, value);
                    flag = false;
                }
                continue;
            }
            if(split[i].contains("form-data")){
                String[] formSplit = split[i].split(";");
                String[] kvSplit = formSplit[formSplit.length - 1].trim().split("=");
                if (kvSplit.length != 2){
                    return;
                }
                if(!kvSplit[0].trim().equals("name")){
                    return;
                }
                key = kvSplit[1];
                flag = true;
            }
        }
        System.out.println(res);

    }



}
