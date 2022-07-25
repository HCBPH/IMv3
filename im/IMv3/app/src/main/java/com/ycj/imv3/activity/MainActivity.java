package com.ycj.imv3.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ycj.imv3.R;
import com.ycj.imv3.config.IMConfiguration;
import com.ycj.imv3.service.IMService;

public class MainActivity extends AppCompatActivity {

    EditText et_uname;
    EditText et_uid;
    EditText et_pwd;
    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.i("MainActivity", Thread.currentThread().getName());
                IMConfiguration.USERNAME_SELF = et_uname.getText().toString();
                IMConfiguration.UID_SELF = Long.parseLong(et_uid.getText().toString());
                IMConfiguration.PASSWORD_SELF = et_pwd.getText().toString();
                Intent serviceIntent = new Intent(MainActivity.this, IMService.class);
                startForegroundService(serviceIntent);
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });


    }

    private void initView(){
        et_uname = findViewById(R.id.login_et_uname);
        et_uid = findViewById(R.id.login_et_id);
        et_pwd = findViewById(R.id.login_et_pwd);
        btn_start = findViewById(R.id.login_btn_start);
    }


}