package com.ycj.imv3.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycj.imv3.R;
import com.ycj.imv3.adapter.ChatListAdapter;
import com.ycj.imv3.adapter.OnlineAdapter;
import com.ycj.imv3.apojo.User;
import com.ycj.imv3.service.IMService;
import com.ycj.imv3.starter.IMClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.netty.util.internal.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    Button btn_state;
    Button btn_refresh;
    TextView tv_state_client;
    TextView tv_state_thread;
    TextView tv_state_thread_name;
    ListView lv_refresh;
    ListView lv_chatlist;

    ArrayList<User> onlineUsers;
    ArrayList<User> chatList;
    OnlineAdapter onlineAdapter;
    ChatListAdapter chatListAdapter;

    IMService imService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intentToService = new Intent(HomeActivity.this, IMService.class);
        bindService(intentToService, connection, Context.BIND_AUTO_CREATE);

        initView();

        btn_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOnlineUsers();
            }
        });



    }

    private void initView(){
        btn_state = findViewById(R.id.home_btn_state);
        btn_refresh = findViewById(R.id.home_btn_refresh);
        tv_state_client = findViewById(R.id.home_tv_state_client);
        tv_state_thread = findViewById(R.id.home_tv_state_thread);
        tv_state_thread_name = findViewById(R.id.home_tv_state_thread_name);
        lv_refresh = findViewById(R.id.home_listview_refresh);
        lv_chatlist = findViewById(R.id.home_listview_list);

        onlineUsers = new ArrayList<>();
        chatList = new ArrayList<>();
        // TODO:在进入HomeActivity的时候去获取数据更新chatlist

        onlineAdapter = new OnlineAdapter(this, onlineUsers, chatList);
        chatListAdapter = new ChatListAdapter(this, chatList);

        lv_refresh.setAdapter(onlineAdapter);
        lv_chatlist.setAdapter(chatListAdapter);
    }

    private void updateState(){
        Thread clientThread = imService.getClientThread();
        tv_state_client.setText("IMv3状态:"+IMClient.getInstance().state().toString());
        tv_state_thread_name.setText("服务线程:"+clientThread.getName());
        tv_state_thread.setText("线程状态:"+clientThread.getState().toString());
    }

    private void updateOnlineUsers(){
        OnlineMemberTask task = new OnlineMemberTask();
        task.execute();
    }

    private void updateChatList(){
        // TODO:获取数据，更新chatlist，再notify
        chatList.add(new User(0L, "test"));
        chatListAdapter.notifyDataSetChanged();
    }



    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("HomeActivity", "绑定service");
            IMService.IMBinder binder = (IMService.IMBinder) service;
            imService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("HomeActivity", "service解除绑定");
        }
    };

    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {

        }
    };


    @SuppressLint("StaticFieldLeak")
    private class OnlineMemberTask extends AsyncTask<Long, Long, Long>{

        @Override
        protected Long doInBackground(Long... longs) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url("http://www.ycj.asia:29233/user/online")
                    .get()
                    .build();

//            client.newCall(request).execute() //同步请求
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("HomeActivity", "请求在线用户失败");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HomeActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String s = response.body().string();
                    Log.i("HomeActivity", s);
                    HashMap<String, String> user = new ObjectMapper().readValue(s, HashMap.class);
                    for (String u : user.keySet()) {
                        String uname = (String) user.get(u);
                        Log.i("HomeActivity", ""+u);
                        onlineUsers.add(new User(Long.parseLong(u), uname));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onlineAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });

            return null;
        }
    }
}
