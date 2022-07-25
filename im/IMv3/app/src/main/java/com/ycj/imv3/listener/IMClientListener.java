package com.ycj.imv3.listener;

public interface IMClientListener {

    public void afterBoot(boolean isSuccess);

    public void afterStop(boolean isSuccess);

    public void stateChange(String s);

}
