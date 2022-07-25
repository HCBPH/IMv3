package com.ycj.im.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import com.google.gson.Gson;
import com.ycj.client.NettyClient;
import com.ycj.common.Common;
import com.ycj.entity.ChatEntity;
import com.ycj.entity.JsonEntity;
import com.ycj.entity.Message;
import com.ycj.entity.Response;
import com.ycj.im.R;
import com.ycj.im.adapter.ChatAdapter;
import com.ycj.listener.NettyListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GroupActivity extends AppCompatActivity implements NettyListener {

    private String GID;
    NettyClient client;

    private ListView listView;
    private EditText et;
    private Button btn_send;
    private Button btn_select;

    ChatAdapter adapter;
    ArrayList<ChatEntity> data = new ArrayList<>();

    private void initView(){
        this.listView = findViewById(R.id.group_lv);
        this.et = findViewById(R.id.group_input);
        this.btn_send = findViewById(R.id.group_send);
        this.btn_select = findViewById(R.id.group_select);

        adapter = new ChatAdapter(this, data);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent intent = getIntent();
        GID = intent.getStringExtra("gid");
        client = Common.client;
        client.setListener(this);

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
                    Toast.makeText(GroupActivity.this, "未与服务器连接", Toast.LENGTH_SHORT).show();
                }else {
                    String msg = et.getText().toString();
                    et.setText("");
                    try {
                        if (msg.length() != 0) {
                            client.sendGroup(GID, msg);
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
                    if(t.contains(".jpg") || t.contains(".png") || t.contains(".jpeg")){
                        data.add(new ChatEntity(1, false, message.getUsername(), "", t));
                    }else{
                        data.add(new ChatEntity(0, false, message.getUsername(), "", t));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 1: // 接收到netty的response
                    Response response = (Response) msg.obj;
                    Toast.makeText(GroupActivity.this, response.getState(), Toast.LENGTH_SHORT).show();
                    break;
                case 2: // state监听
//                    Log.d("chat|obj:", msg.obj.toString());
                    int code = (int) msg.obj;
                    if(code == 1){
                        Toast.makeText(GroupActivity.this, "连接成功!!!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(GroupActivity.this, "与服务器断开连接", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3: // 发送图片失败
                    Toast.makeText(GroupActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Log.d("case 4", (String) msg.obj);
                    client.sendGroup(GID, (String) msg.obj);
                    data.add(new ChatEntity(1, true, client.getUID(), "", (String) msg.obj));
                    adapter.notifyDataSetChanged();
            }
        }
    };


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
        Log.d("GroupActivity", msg.getData().toString());
        android.os.Message m = new android.os.Message();
        if (msg.getDst().equals(GID) && !msg.getSrc().equals(Common.UID)){
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
}