package com.ycj.handler;

import com.ycj.entity.Heartbeat;
import com.ycj.entity.Message;
import com.ycj.entity.Request;
import com.ycj.entity.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author 53059
 * @date 2021/6/4 20:46
 */
public class NettyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        System.out.println("[Decoder]:WORKING......");
        in.markReaderIndex();
        // 传过来的ByteBuf中包含三个字段：类型、长度和数据（message、request或者response）
        int type = in.readInt();
        int length = in.readInt();
        byte[] data = new byte[length];
        in.readBytes(data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        // 接下来解析数据类型，将data转换成一个实体类
        if (type == 2) {
            Message msg = (Message) objectInputStream.readObject();
            out.add(msg);
        } else if (type == 1) {
            Response msg = (Response) objectInputStream.readObject();
            out.add(msg);
        } else if (type == 0) {
            Request msg = (Request) objectInputStream.readObject();
            out.add(msg);
        } else if(type == 3){
            Heartbeat msg = (Heartbeat) objectInputStream.readObject();
            out.add(msg);
        }else {
            System.out.println("[Decoder]:类型出错");
            throw new RuntimeException();
        }
        objectInputStream.close();
        inputStream.close();
    }
}
