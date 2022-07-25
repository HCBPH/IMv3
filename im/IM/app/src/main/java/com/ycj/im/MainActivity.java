package com.ycj.im;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ycj.client.NettyClient;
import com.ycj.common.Common;
import com.ycj.entity.Message;
import com.ycj.entity.Response;
import com.ycj.im.activity.ChatActivity;
import com.ycj.im.activity.HomeActivity;
import com.ycj.listener.NettyListener;

import java.io.IOException;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    private EditText et;
    private Button btn;

    private void initView(){
        this.et = findViewById(R.id.main_id);
        this.btn = findViewById(R.id.main_connect);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        initView();

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0){
                    btn.setEnabled(false);
                }else{
                    btn.setEnabled(true);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = et.getText().toString();
                if(uid.length() != 0){
                    Common.client = NettyClient.getInstance(uid, new NettyListener() { //向服务器注册，还不能和别人聊天
                        @Override
                        public void onStateChange(int code) {

                        }

                        @Override
                        public void onReceiveMsg(Message msg) {
                            Log.d("MainActivity", "first listener");
                        }

                        @Override
                        public void onReceiveMsg(Response response) {

                        }
                    });
                    Common.client.start();
                    Common.UID = uid;
                    et.setText("");
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}