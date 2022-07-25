package com.ycj.imv3.util;

/**
 * @author 53059
 * @date 2022/1/17 23:47
 */
public class Comparator {
    public static boolean compare(byte[] t1, byte[] t2){
        if (t1.length != t2.length){
            return false;
        }
        for(int i=0;i<t1.length;i++){
            if (t1[i] != t2[i]){
                return false;
            }
        }
        return true;
    }
}
