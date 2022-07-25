package com.ycj.imv3.listener;

import com.ycj.imv3.entity.GroupMessageOuterClass;
import com.ycj.imv3.entity.MessageOuterClass;

public interface MessageListener {

    // Message类包含:
    // from:谁发过来
    // to:发给谁，可以用来判断是否是垃圾消息(理论上不存在，但以防万一)
    // content:具体发送的文字
    // timestamp:时间戳，长整型，有需要可以自己转化为时间格式
    public void receiveMessage(MessageOuterClass.Message msg);

    // GroupMessage和Message类似，一是为了方便区分是单发还是群发消息，二是里面的from意义不同，它的from是gid
    public void receiveGroupMessage(GroupMessageOuterClass.GroupMessage msg);
}
