package com.ycj.imv3.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycj.imv3.R;
import com.ycj.imv3.activity.ChatActivity;
import com.ycj.imv3.apojo.User;

import java.util.ArrayList;

public class OnlineAdapter extends BaseAdapter {

    ArrayList<User> onlineMembers;
    Context context;
    ArrayList<User> chatList;

    public OnlineAdapter(Context context, ArrayList<User> data, ArrayList<User> chatList){
        this.context = context;
        this.onlineMembers = data;
        this.chatList = chatList;
    }

    public void setData(ArrayList<User> data){
        this.onlineMembers = data;
    }

    @Override
    public int getCount() {
        return onlineMembers.size();
    }

    @Override
    public Object getItem(int position) {
        return onlineMembers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            // 第一次inflate view
            view = View.inflate(context, R.layout.listview_online, null);
            viewHolder = new ViewHolder(
                    view.findViewById(R.id.listview_online_img),
                    view.findViewById(R.id.listview_online_username)
            );
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // TODO:获取服务端的头像
        // onlineMembers.get(position).getProfile()
        viewHolder.imageView.setImageResource(R.drawable.profile2);
        viewHolder.tv_username.setText(onlineMembers.get(position).getUsername());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:点击在线列表，创建一个聊天时，给聊天列表的data添加数据，但还没刷新adapter，这时我们已经跳转到了ChatActivity，所以在跳转前，HomeActivity要做数据的缓存（临时或者本地），跳转后，再回到HomeActivity，读取缓存更新页面。
                chatList.add(onlineMembers.get(position));
                Intent intentToChat = new Intent(context.getApplicationContext(), ChatActivity.class);
                intentToChat.putExtra("uid", onlineMembers.get(position).getUid());
                intentToChat.putExtra("username", onlineMembers.get(position).getUsername());
                intentToChat.putExtra("profile", onlineMembers.get(position).getProfile());
                // TODO:了解activity启动的标志，达到任务栈中有且仅有一个，且每次调用恢复之前的数据（数据库和临时缓存）
                intentToChat.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intentToChat);
            }
        });
        return view;
    }

    private static class ViewHolder{
        ImageView imageView;
        TextView tv_username;

        public ViewHolder(ImageView imageView, TextView tv_username){
            this.imageView = imageView;
            this.tv_username = tv_username;

        }
    }
}
