package com.ycj.imv2.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

public class GuardService extends Service {

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("GuardService", "绑定守护进程");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("GuardService", "和守护进程断开");
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("GuardService", "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("GuardService", "onStartCommand");
        startForeground(1, new Notification());
        if (!serviceAlive(ImService.class.getName())){
           bindService(new Intent(GuardService.this, ImService.class), connection, Context.BIND_IMPORTANT);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("GuardService", "onDestroy");
        bindService(new Intent(GuardService.this, ImService.class), connection, Context.BIND_IMPORTANT);
        super.onDestroy();
    }


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
}
