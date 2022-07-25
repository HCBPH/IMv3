package com.ycj.imv3.test;

import com.ycj.imv3.starter.IMServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 53059
 * @date 2022/1/14 14:34
 */
public class ServerTest {

    static Logger log = LogManager.getLogger();

    public static void main(String[] args) {


        IMServer server = IMServer.getInstance();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                server.run();
            }
        });
        t.start();

    }

}
