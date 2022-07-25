package com.ycj.imv2.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ycj.client.NettyClient;
import com.ycj.entity.Message;
import com.ycj.entity.Response;
import com.ycj.imv2.common.Constant;
import com.ycj.listener.MessageStateListener;
import com.ycj.listener.NettyListener;

import java.util.ArrayList;
import java.util.List;

public class ImService extends Service {

    public static NettyClient client;
    public static ImService instance;
    public static ArrayList<Context> contexts = new ArrayList<>();
    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ImService", "绑定守护进程");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ImService", "和守护进程断开");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ImService", "onCreate");
        super.onCreate();
        client = NettyClient.getInstance(Constant.UID, new NettyListener() {
            @Override
            public void onReceiveMsg(Message msg) {
                Log.d("ImService", "收到消息--"+msg.getSrc()+"--"+msg.getData());
            }

            @Override
            public void onReceiveMsg(Response response) {
                Log.d("ImService", "收到响应--"+response.getState());
            }

            @Override
            public void onStateChange(int code) {
                if(code == 1){
                    Log.d("ImService", "与服务器连接成功");
                }else{
                    Log.d("ImService", "与服务器断开连接");
                }
            }
        });
        client.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("ImService", "onBind");
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ImService", "onStartCommand");
        startForeground(1, new Notification());
        if (!serviceAlive(GuardService.class.getName())){
            bindService(new Intent(ImService.this, GuardService.class), connection, Context.BIND_IMPORTANT);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("ImService", "onDestroy");
        bindService(new Intent(ImService.this, GuardService.class), connection, Context.BIND_IMPORTANT);
        super.onDestroy();
    }




    /**
     * 监听器
     * */





    /**
     * 自定义方法
     * */

    private boolean serviceAlive(String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public void addContexts(Context context){
        contexts.add(context);
    }

    public class MyBinder extends Binder{
        public ImService getService(){
            synchronized (ImService.class){
                if (instance == null){
                    instance = new ImService();
                }
            }
            return instance;
        }
    }
}
