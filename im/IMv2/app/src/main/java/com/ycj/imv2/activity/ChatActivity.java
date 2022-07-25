package com.ycj.imv2.activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.ycj.client.NettyClient;
import com.ycj.common.Common;
import com.ycj.entity.ChatEntity;
import com.ycj.entity.JsonEntity;
import com.ycj.entity.Message;
import com.ycj.entity.Response;
import com.ycj.imv2.R;
import com.ycj.imv2.adapter.ChatAdapter;
import com.ycj.listener.NettyListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ChatActivity extends AppCompatActivity implements NettyListener {

    private String DST;
    NettyClient client;

    private ListView listView;
    private EditText et;
    private Button btn_send;
    private Button btn_select;

    ChatAdapter adapter;
    ArrayList<ChatEntity> data = new ArrayList<>();

    private void sendNotification(String msg, String uname){
        NotificationManagerCompat manager;
        NotificationChannel channel;
        NotificationCompat.Builder builder;
        Notification notification;
        String cid = String.valueOf(System.currentTimeMillis());

        // 先创建一个manager，后面通道、通知都是通过manager注册
        manager = NotificationManagerCompat.from(this);

        // 申请个权限
        if(!manager.areNotificationsEnabled()){
            Log.d("ChatActivity", "没有通知栏权限");
        }

        // 再创建一个消息通知的通道，包含：唯一ID，名字（描述），通道的消息等级
        channel = new NotificationChannel(
               cid , "SINGLE_CHAT", NotificationManager.IMPORTANCE_HIGH
        );
        manager.createNotificationChannel(channel);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //sdk版本>=26 android8
            builder = new NotificationCompat.Builder(getApplicationContext(), cid);
        }else{ //sdk版本<26
            builder = new NotificationCompat.Builder(this);
            // 还要设置通知的级别，上面android8以上的在channel中已经设好了，但8以下的要在builder中设置
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // sdk版本>=24 android7 - 8之间
                builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
            }else{
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }
        }

        Intent intent = new Intent(this, ChatActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 5, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notification = builder
                .setContentTitle(this.DST)
                .setSmallIcon(R.drawable.dragon)
                .setContentText(msg)
                .setTicker("这是测试")
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(new Random().nextInt(), notification);

    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        Log.d("debug", "onSaveInstanceState");
        for (ChatEntity entity:data){
            Log.d("保存的数据", entity.getData());
        }
        outState.putParcelableArrayList("data", data);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        Log.d("debug", "onStart");
        super.onStart();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d("debug", "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        data = savedInstanceState.getParcelableArrayList("data");
        for (ChatEntity entity:data){
            Log.d("收到的数据", entity.getData());
        }
    }

    @Override
    protected void onResume() {
        Log.d("debug", "onResume");
        super.onResume();
        for (ChatEntity entity:data){
            Log.d("恢复的数据", entity.getData());
        }
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    private void initView(){
        this.listView = findViewById(R.id.chat_lv);
        this.et = findViewById(R.id.chat_input);
        this.btn_send = findViewById(R.id.chat_send);
        this.btn_select = findViewById(R.id.chat_select);
    }

    @Override
    protected void onDestroy() {
        Log.d("ChatActivity", "销毁");
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        DST = intent.getStringExtra("dst");
        client = Common.client;
        client.setListener(this);

        initView();
        adapter = new ChatAdapter(this, data);
        listView.setAdapter(adapter);


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
                    btn_send.setEnabled(false);
                }else{
                    btn_send.setEnabled(true);
                }
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先判断client是否连接
                if (!client.isStart()){
                    Toast.makeText(ChatActivity.this, "未与服务器连接", Toast.LENGTH_SHORT).show();
                }else {
                    String msg = et.getText().toString();
                    et.setText("");
                    try {
                        if (msg.length() != 0) {
                            client.sendMsg(DST, msg);
                            data.add(new ChatEntity(0, true, client.getUID(), "", msg));
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (resultCode == RESULT_OK){
                    Uri img_uri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(img_uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String img_path = cursor.getString(columnIndex);
                    cursor.close();

                    File file = new File(img_path);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10,TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .build();
                    MediaType type = MediaType.parse("image/png");
                    RequestBody fileBody = RequestBody.create(file, type);
                    RequestBody multipartBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", "", fileBody)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://www.ycj.asia:8080/im/img")
                            .post(multipartBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            android.os.Message m = new android.os.Message();
                            m.what = 3;
                            e.printStackTrace();
                            handler.sendMessage(m);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                            android.os.Message m = new android.os.Message();
                            String res = Objects.requireNonNull(response.body()).string();
                            m.what = 4;

                            Gson gson = new Gson();
                            JsonEntity entity = gson.fromJson(res, JsonEntity.class);
                            m.obj = entity.getImg();
                            handler.sendMessage(m);
                            Log.d("chat|response:", entity.getImg());
                        }
                    });
                }
                break;
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0: // 接收到信息
                    Message message = (Message) msg.obj;
                    String t = (String) message.getData();
                    String uname = message.getUsername();
                    Log.d("chat|handler", t);

                    if(t.contains(".jpg") || t.contains(".png") || t.contains(".jpeg")){
                        data.add(new ChatEntity(1, false, uname, "", t));
                    }else{
                        data.add(new ChatEntity(0, false, uname, "", t));
                    }
//                    for (ChatEntity entity:data){
//                        Log.d("监听更新后的数据", entity.getData());
//                    }
                    // 如果处于后台，就从通知栏提醒
                    if (isBackground()){
                        sendNotification(t, uname);

                        Log.d("ChatActivity", "后台处理");
                    }else{
                        adapter.notifyDataSetChanged();
                        Log.d("ChatActivity", "前台处理");
                    }
                    break;
                case 1: // 接收到netty的response
                    Response response = (Response) msg.obj;
                    Toast.makeText(ChatActivity.this, response.getState(), Toast.LENGTH_SHORT).show();
                    break;
                case 2: // state监听
//                    Log.d("chat|obj:", msg.obj.toString());
                    int code = (int) msg.obj;
                    if(code == 1){
                        Toast.makeText(ChatActivity.this, "连接成功!!!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChatActivity.this, "与服务器断开连接", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3: // 发送图片失败
                    Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Log.d("case 4", (String) msg.obj);
                    client.sendMsg(DST, (String) msg.obj);
                    data.add(new ChatEntity(1, true, client.getUID(), "", (String) msg.obj));
                    adapter.notifyDataSetChanged();
            }
        }
    };


    /**
     * 判断是否处于后台
     * */
    private boolean isBackground(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        boolean isBackground = true;
        String processName = "";
        for (ActivityManager.RunningAppProcessInfo appProcessInfo:appProcessInfos){
            if(appProcessInfo.processName.equals(getPackageName())){
                processName = appProcessInfo.processName;
//                Log.d("debug", processName);
                if(appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED){
                    isBackground = true;
                }else if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                || appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE){
                    isBackground = false;
                }else{
                    isBackground = true;
                }
            }
        }
        return isBackground;
    }


    /**
     * 监听他人发来的消息
     * */

    @Override
    public void onStateChange(int code) {
        android.os.Message m = new android.os.Message();
        m.what = 2;
        m.obj = code;
        handler.sendMessage(m);
    }

    @Override
    public void onReceiveMsg(Message msg) {
//        Toast.makeText(ChatActivity.this, msg.getData().toString(), Toast.LENGTH_SHORT).show();
        Log.d("ChatActivity", msg.getData().toString());
        android.os.Message m = new android.os.Message();
        if (msg.getSrc().equals(DST)){
            m.what = 0;
            m.obj = msg;
            handler.sendMessage(m);
        }
    }

    @Override
    public void onReceiveMsg(Response response) {
        android.os.Message m = new android.os.Message();
        m.what = 1;
        m.obj = response;
        handler.sendMessage(m);
    }

    @Override
    protected void onStop() {
        Log.d("debug", "onStop()");
        super.onStop();
    }
}