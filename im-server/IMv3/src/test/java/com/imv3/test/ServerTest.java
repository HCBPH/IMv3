package com.imv3.test;

import com.ycj.imv3.starter.IMServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;

/**
 * @author 53059
 * @date 2022/1/12 23:22
 */
public class ServerTest {

    private static Logger log = LogManager.getLogger();
    IMServer server;

    @Test
    public void runServer(){
        server = IMServer.getInstance();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                server.run();
            }
        });
        t.start();
    }

    @After
    public void stopServer(){
        server.stop();
    }
}
