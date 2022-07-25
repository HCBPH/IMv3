package com.ycj.imv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ycj.imv2.activity.ListActivity;
import com.ycj.imv2.common.Constant;
import com.ycj.common.Common;
import com.ycj.imv2.service.GuardService;
import com.ycj.imv2.service.ImService;

public class MainActivity extends AppCompatActivity {

    private EditText et_uname;
    private Button btn_login;

    ImService imService;

    /**
     * 创建service连接
     * */
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("MainActivity", "连接netty service");
            ImService.MyBinder binder = (ImService.MyBinder) service;
            imService = binder.getService();
            imService.addContexts(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MainActivity", "与netty service断开连接");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_uname = findViewById(R.id.main_uname);
        btn_login = findViewById(R.id.main_login);

        askForPermission();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = et_uname.getText().toString();
                et_uname.setText("");

                Constant.UID = uname;

                // 启动netty service和守护进程
                Intent intent_service = new Intent(MainActivity.this, ImService.class);
                Intent intent_guard = new Intent(MainActivity.this, GuardService.class);
                startService(intent_service);
                startService(intent_guard);

                // 和netty service绑定，传context过去
                bindService(intent_service, connection, Context.BIND_IMPORTANT);


                Intent intent_activity = new Intent(MainActivity.this, ListActivity.class);
                intent_activity.putExtra("uanme", uname);
                startActivity(intent_activity);
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "onDestroy");
        unbindService(connection);
        super.onDestroy();
    }

    /**
     * 申请权限
     * */
    private void askForPermission(){
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
}