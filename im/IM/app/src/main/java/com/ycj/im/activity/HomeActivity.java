package com.ycj.im.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ycj.client.NettyClient;
import com.ycj.common.Common;
import com.ycj.im.MainActivity;
import com.ycj.im.R;

public class HomeActivity extends AppCompatActivity {

    private EditText et;
    private Button btn;
    private EditText et_group;
    private Button btn_create;
    private Button btn_connect;
    private Button btn_quit;


    private void initView(){
        this.et = findViewById(R.id.home_id);
        this.btn = findViewById(R.id.home_connect);
        this.et_group = findViewById(R.id.home_group);
        this.btn_create = findViewById(R.id.home_create_group);
        this.btn_connect = findViewById(R.id.home_connect_group);
        this.btn_quit = findViewById(R.id.home_quit_group);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        //单人
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dst = et.getText().toString();
                if(dst.length() != 0){
                    et.setText("");
                    Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                    intent.putExtra("dst", dst);
                    startActivity(intent);
                }
            }
        });






        et_group.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0){
                    btn_connect.setEnabled(false);
                    btn_create.setEnabled(false);
                    btn_quit.setEnabled(false);
                }else{
                    btn_connect.setEnabled(true);
                    btn_create.setEnabled(true);
                    btn_quit.setEnabled(true);
                }
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gid = et_group.getText().toString();
                if(gid.length() != 0){
                    et.setText("");
                    Intent intent = new Intent(HomeActivity.this, GroupActivity.class);
                    intent.putExtra("gid", gid);
                    // 创建聊天小组
                    Common.client.createGroup(gid);
                    startActivity(intent);
                }
            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gid = et_group.getText().toString();
                if(gid.length() != 0){
                    et.setText("");
                    Intent intent = new Intent(HomeActivity.this, GroupActivity.class);
                    intent.putExtra("gid", gid);
                    // 加入小组
                    Common.client.joinGroup(gid);
                    startActivity(intent);
                }
            }
        });

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gid = et_group.getText().toString();
                if(gid.length() != 0){
                    et.setText("");
                    // 退出组
                    Common.client.quitGroup(gid);
                }
            }
        });
    }
}