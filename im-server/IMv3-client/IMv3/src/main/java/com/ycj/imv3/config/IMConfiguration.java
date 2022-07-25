package com.ycj.imv3.config;

/**
 * @author 53059
 * @date 2022/1/12 15:23
 */
public class IMConfiguration {


    // IMClient连接服务器相关的配置和用户信息
    public static int PORT_SERVER = 29233;
    public final static long UID_SERVER = 0L;
    public static final byte[] SEPARATOR = {127, -2};
    public static final long GID_DIGITAL_NUMBER = 1000000000L;

    // client 用户信息
    public static String ADDR_SERVER = "localhost";
    public static long UID_SELF = 1L;
    public static String USERNAME_SELF = "computer";
    public static String PASSWORD_SELF = "admin";


    // 心跳相关配置
    public static final int HEARTBEAT_TIME_READ = 20;
    public static final int HEARTBEAT_TIME_WRITE = 15;
    public static final int TYPE_HEARTBEAT_HEALTHY = 10071;
    public static final int TYPE_HEARTBEAT_UNHEALTHY = 10072;


    /*
       ----------------------------------------
      | separator | type | data_size | data  |
     |  2Bytes   | int  | int(Byte) | nBytes|
    ---------------------------------------
     */
    // 解包时的字段
    public static final int TYPE_USER = 10010;
    public static final int TYPE_INFO = 10020;
    public static final int TYPE_LOGIN = 10030;
    public static final int TYPE_RESPONSE = 10040;
    public static final int TYPE_MESSAGE = 10050;
    public static final int TYPE_MESSAGE_GROUP = 10060;
    public static final int TYPE_HEARTBEAT = 10070;


    // Http返回结果的json字段
    public static final String RESULT_KEY_STATE = "state";
    public static final String RESULT_KEY_GID = "gid";
    public static final String RESULT_KEY_REASON = "reason";
    public static final String RESULT_VALUE_SUCCESS = "1";
    public static final String RESULT_VALUE_FAIL = "0";


    // IMClient的状态
    public enum State{
        RUNNING,
        SHUTDOWN
    }


    // response回复类型
    public static final int RESPONSE_TYPE_LOGIN_SUCCESS = 10041;
    public static final int RESPONSE_TYPE_LOGIN_FAIL = 10042;
    public static final int RESPONSE_TYPE_MSG_SUCCESS = 10043;
    public static final int RESPONSE_TYPE_MSG_FAIL = 10044;
    public static final int RESPONSE_TYPE_DST_NOT_ONLINE = 10045;
    public static final int RESPONSE_TYPE_DST_NOT_FOUND = 10046;
    public static final int RESPONSE_TYPE_GROUP_NOT_FOUND = 10047;
    public static final int RESPONSE_TYPE_NO_AUTH_IN_GROUP = 10048;
    public static final int RESPONSE_TYPE_NO_LOGIN = 10049;



}
