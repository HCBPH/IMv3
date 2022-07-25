# 肝不动了

### 一、IMv3客户端基本使用

首先去找test/ClientTest.java，整体的流程参照里面就行了



### 二、配置

配置文件都在config/IMConfiguration.java文件中，可以实现动态配置

能配置些啥，具体参考文件中内容，这里只举两个例子：

```java
// 服务器地址，测试用的是本地的，实际使用1.117.74.41
public static String ADDR_SERVER = "localhost";

// 用户uid，和web USER表中的uid对应
public static long UID_SELF = 1L;

// 用户名称
public static String USERNAME_SELF = "computer";

// 密码，连接server需要登录
public static String PASSWORD_SELF = "admin";

// 心跳写频率，定位信息夹杂在心跳中发送，下面的是每个15秒发送一次
// 还有一个是心跳读频率，服务器没20s读取一次，如果20s没有收到client任何消息，断开连接
public static final int HEARTBEAT_TIME_WRITE = 15;
```



### 三、补充说明

由于Android好久没碰，写起来不熟练，加上时间紧，就没有实现android的用例。所以图片发送的功能也相当于没有实现，因为我也不确定现在lab web上是否有对应图片接口，要实现就得   使用特殊字段+web图片接口   或者   在设计一个消息类+web图片接口

