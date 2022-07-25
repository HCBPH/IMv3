package com.ycj.handler;

import com.ycj.entity.Heartbeat;
import com.ycj.entity.Message;
import com.ycj.entity.Request;
import com.ycj.entity.Response;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author 53059
 * @date 2021/6/4 20:45
 */
public class NettyEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 首先将msg也就是数据，转化成字节流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(msg);
        objectOutputStream.flush();
        byte[] bytes = outputStream.toByteArray();
        // 设置类型字段
        if (msg instanceof Message) {
            out.writeInt(2);
        } else if (msg instanceof Request) {
            out.writeInt(0);
        } else if (msg instanceof Response) {
            out.writeInt(1);
        } else if (msg instanceof Heartbeat){
            out.writeInt(3);
        }else {
            System.out.println("[Encoder]:字段类型出错");
            out.writeInt(-1);
        }
        // 设置长度字段
        out.writeInt(bytes.length);

        // 设置数据字段
        out.writeBytes(bytes);

        objectOutputStream.close();
        outputStream.close();
    }
}
