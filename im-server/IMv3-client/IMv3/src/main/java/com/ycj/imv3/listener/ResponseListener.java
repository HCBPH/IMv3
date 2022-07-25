package com.ycj.imv3.listener;

import com.ycj.imv3.entity.ResponseOuterClass;

public interface ResponseListener {
    public void receiveResponse(ResponseOuterClass.Response response);
}
