package com.ycj.imv3.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.ycj.imv3.R;
import com.ycj.imv3.activity.HomeActivity;
import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.listener.IMClientListener;
import com.ycj.imv3.starter.IMClient;

import java.util.Date;
import java.util.Hashtable;

public class IMService extends Service implements IMClientListener {

    NotificationManagerCompat manager;
    NotificationChannel channel;
    Intent intentToMain;
    Thread clientThread;

    Hashtable<Long, Handler> chatHandlers;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        manager = NotificationManagerCompat.from(this);
        if (!manager.areNotificationsEnabled()){
            Toast.makeText(getApplicationContext(), "没有权限", Toast.LENGTH_SHORT).show();
        }
        channel = new NotificationChannel("com.ycj.im", "service_channel", NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
        intentToMain = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intentToMain, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(this, "com.ycj.im")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon)
                .setContentText("IMv3正在运行中")
                .setContentTitle("IMv3")
                .build();
        startForeground(1, notification);
        manager.notify(1, notification);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IMBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                IMClient.getInstance().run(IMConfiguration.USERNAME_SELF, IMConfiguration.UID_SELF, IMConfiguration.PASSWORD_SELF, IMService.this);
            }
        });
        clientThread.start();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        IMClient.getInstance().stop();
        Toast.makeText(this, "IMService 关闭", Toast.LENGTH_SHORT).show();
    }

    Handler serviceHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    // IMv3启动的相关消息
                    Toast.makeText(IMService.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    Log.w("IMService", (String) msg.obj);
                    break;
                case 1:
                    break;
                default:
                    Toast.makeText(IMService.this, "unknown message, handler can't deal with.", Toast.LENGTH_SHORT).show();
                    Log.w("IMService", "unknown message, handler can't deal with.");
                    break;
            }
        }
    };

    public void insertChatHandler(Long uid, Handler handler){
        if (chatHandlers.containsKey(uid)){
            Log.w("IMService", "handler已经存在,不能重复添加!");
        }else{
            chatHandlers.put(uid, handler);
            Log.i("IMService", uid+"注册到service");
        }
    }

    public class IMBinder extends Binder{
        public IMService getService(){
            return IMService.this;
        }
    }
    
    public Thread getClientThread(){
        return clientThread;
    }




/*
* BootstrapListener
* */
    @Override
    public void afterBoot(boolean isSuccess) {
        Message message = new Message();
        if (isSuccess){
            message.what = 0;
            message.obj = "IMv3启动, " + new Date();
        }else{
            message.what = 0;
            message.obj = "IMv3启动失败, " + new Date();
        }
        serviceHandler.sendMessage(message);
    }

    @Override
    public void afterStop(boolean isSuccess) {
        Message message = new Message();
        message.what = 0;
        message.obj = "IMv3关闭, " + new Date();
        serviceHandler.sendMessage(message);
    }

    @Override
    public void stateChange(String s) {
        Message message = new Message();
        message.what = 0;
        message.obj = s;
        serviceHandler.sendMessage(message);
    }
}
