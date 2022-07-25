package com.ycj.imv3.listener;

import com.ycj.imv3.entity.Location;

public interface HeartbeatListener {

    public Location sendHeartbeat();

    public void receiveHeartbeat();
}
