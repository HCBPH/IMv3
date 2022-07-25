package com.ycj.imv3.test;

import com.ycj.imv3.starter.IMClient;

import java.util.Scanner;

/**
 * @author 53059
 * @date 2022/1/19 0:54
 */
public class ClientTest {
    public static void main(String[] args) {
        IMClient client = IMClient.getInstance();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
//                client.run();
            }
        });
        t.start();
        Scanner scanner = new Scanner(System.in);
        String s;
        while (scanner.hasNext()){
            System.out.print("[client]:");
            s = scanner.nextLine();
            client.sendMsg(5, s);
        }
    }
}
