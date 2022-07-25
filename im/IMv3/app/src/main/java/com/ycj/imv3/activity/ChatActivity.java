package com.ycj.imv3.activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import com.ycj.imv3.apojo.ChatEntity;
import com.ycj.imv3.db.MySQLiteHelper;
import com.ycj.imv3.entity.ChatEntity;
import com.ycj.imv3.entity.JsonEntity;
import com.ycj.entity.Message;
import com.ycj.entity.Response;
import com.ycj.imv3.R;
import com.ycj.imv3.adapter.ChatAdapter;
import com.ycj.imv3.service.ImService;
import com.ycj.listener.NettyListener;
import com.ycj.util.StringUtil;

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

public class ChatActivity extends AppCompatActivity{

    private String DST;

    private ListView listView;
    private EditText et;
    private Button btn_send;
    private Button btn_select;

    ChatAdapter adapter;
    ArrayList<ChatEntity> data = new ArrayList<>();

    MySQLiteHelper helper;
    SQLiteDatabase db;


    ImService imService;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StringUtil.out("ChatActivity", "绑定service");
            ImService.MyBinder binder = (ImService.MyBinder) service;
            imService = binder.getService();
            imService.addDst(DST, handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            StringUtil.out("ChatActivity", "service解除绑定");
        }
    };

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
            StringUtil.out("ChatActivity", "没有通知栏权限");
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
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("dst", DST);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setAction(Long.toString(System.currentTimeMillis()));
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
        StringUtil.out("ChatActivity", "onSaveInstanceState");
        for (ChatEntity entity:data){
            StringUtil.out("onSaveInstanceState", "保存数据："+entity.getData());
        }
        outState.putParcelableArrayList("data", data);
        outState.putString("dst", DST);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        StringUtil.out("ChatActivity", "onRestoreInstanceState");
        data = savedInstanceState.getParcelableArrayList("data");
        DST = savedInstanceState.getString("dst");
        for (ChatEntity entity:data){
            StringUtil.out("恢复数据：", entity.getData());
        }
    }

    @Override
    protected void onResume() {
        StringUtil.out("ChatActivity", "onResume");
        Intent intent1 = new Intent(ChatActivity.this, ImService.class);
        bindService(intent1, connection, Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    private void initView(){
        this.listView = findViewById(R.id.chat_lv);
        this.et = findViewById(R.id.chat_input);
        this.btn_send = findViewById(R.id.chat_send);
        this.btn_select = findViewById(R.id.chat_select);
    }

    @Override
    protected void onDestroy() {
        StringUtil.out("ChatActivity", "销毁");
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            StringUtil.out("ChatActivity", "restore savedInstance");
            data = savedInstanceState.getParcelableArrayList("data");
            DST = savedInstanceState.getString("dst");
            for (ChatEntity entity:data){
                StringUtil.out("恢复数据：", entity.getData());
            }
        }

        StringUtil.out("ChatActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        StringUtil.debug(intent.getStringExtra("dst"));
        DST = intent.getStringExtra("dst");


        initView();

        helper = new MySQLiteHelper(this, "im_test", null, 1);
        db = helper.getWritableDatabase();
//        "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='{table_name}';"
        Cursor cursor;
        try {
            cursor = db.query(DST, new String[]{"uname", "message"}, null, null, null, null, null);
        }catch (RuntimeException e){
            e.printStackTrace();
            db.execSQL("create table " + DST + " (id int primary key, uname varchar(20)," +
                    "time timestamp default current_timestamp, message varchar(200))");
            cursor = db.query(DST, new String[]{"uname", "message"}, null, null, null, null, null);

        }
        while(cursor.moveToNext()){
            String uname = cursor.getString(cursor.getColumnIndex("uname"));
            String message = cursor.getString(cursor.getColumnIndex("message"));
            ChatEntity entity;
            if (uname.equals(Common.UID)){
                if(message.contains(".jpg") || message.contains(".png") || message.contains(".jpeg")){
                    entity = new ChatEntity(1, true, uname, "", message);
                }else{
                    entity = new ChatEntity(0, true, uname, "", message);
                }

            }else if (uname.equals(DST)){
                if(message.contains(".jpg") || message.contains(".png") || message.contains(".jpeg")){
                    entity = new ChatEntity(1, false, uname, "", message);
                }else{
                    entity = new ChatEntity(0, false, uname, "", message);
                }
            }else{
                continue;
            }
            data.add(entity);
        }
        adapter = new ChatAdapter(this, data);
        listView.setAdapter(adapter);
        listView.setSelection(data.size() - 1);

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
                if (!NettyClient.getInstance().isStart()){
                    Toast.makeText(ChatActivity.this, "未与服务器连接", Toast.LENGTH_SHORT).show();
                }else {
                    String msg = et.getText().toString();
                    et.setText("");
                    try {
                        if (msg.length() != 0) {
                            NettyClient.getInstance().sendMsg(DST, msg);
                            insertDB(Common.UID, msg);
                            data.add(new ChatEntity(0, true, Common.UID, "", msg));
                            adapter.notifyDataSetChanged();
                            listView.setSelection(data.size() - 1);
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
                    assert data != null;
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
                        }
                    });
                }
                break;
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            Log.i("ChatActivity", Thread.currentThread().getName());
            switch (msg.what){
                case 0: // 连接状态的变化
                    int code = (int) msg.obj;
                    if(code == 1){
                        Toast.makeText(ChatActivity.this, "连接成功!!!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChatActivity.this, "与服务器断开连接", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1: // 接收到netty的response
                    Response response = (Response) msg.obj;
                    Toast.makeText(ChatActivity.this, response.getState(), Toast.LENGTH_SHORT).show();
                    break;
                case 2: // 接收到信息
                    Message message = (Message) msg.obj;
                    String t = (String) message.getData();
                    String uname = message.getUsername();
                    if(t.contains(".jpg") || t.contains(".png") || t.contains(".jpeg")){
                        data.add(new ChatEntity(1, false, uname, "", t));

                    }else{
                        data.add(new ChatEntity(0, false, uname, "", t));
                    }


                    // 如果处于后台，就从通知栏提醒
                    if (isBackground()){
                        sendNotification(t, uname);
                        StringUtil.out("ChatActivity", "后台处理");
                    }else{
                        adapter.notifyDataSetChanged();
                        listView.setSelection(data.size() - 1);
                        StringUtil.out("ChatActivity", "前台处理");
                    }
                    insertDB(DST, t);
                    break;
                case 3: // 图片上传失败
                    Toast.makeText(ChatActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4: // 图片上传成功
                    NettyClient.getInstance().sendMsg(DST, (String) msg.obj);
                    data.add(new ChatEntity(1, true, Common.UID, "", (String) msg.obj));
                    adapter.notifyDataSetChanged();
                    listView.setSelection(data.size() - 1);
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
//                StringUtil.out("debug", processName);
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

    private void insertDB(String uname, String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put("uname", uname);
        contentValues.put("message", content);
        db.insert(DST, null, contentValues);
    }
}