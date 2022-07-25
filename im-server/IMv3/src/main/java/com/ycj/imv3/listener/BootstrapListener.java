package com.ycj.imv3.listener;

public interface BootstrapListener {

    /*
    * 监听IMClient daemon的状态，启动、停止或者是其他变动
    * stateChange暂时没有使用，应为client daemon还相对简单
    * */

    public void afterBoot(boolean isSuccess);

    public void afterStop();

    public void stateChange(String s);

}
