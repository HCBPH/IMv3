package com.ycj.imv3.adapter;

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

public class ChatListAdapter extends BaseAdapter {

    ArrayList<User> chatList;
    Context context;

    public ChatListAdapter(Context context, ArrayList<User> chatList){
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            // 第一次inflate view
            view = View.inflate(context, R.layout.listview_online, null);
            viewHolder = new ViewHolder(
                    view.findViewById(R.id.listview_chatlist_img),
                    view.findViewById(R.id.listview_chatlist_username),
                    view.findViewById(R.id.listview_chatlist_msg)
            );
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // TODO:获取服务端的头像
        // onlineMembers.get(position).getProfile()
        viewHolder.imageView.setImageResource(R.drawable.profile2);
        viewHolder.tv_username.setText(chatList.get(position).getUsername());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToChat = new Intent(context.getApplicationContext(), ChatActivity.class);
                intentToChat.putExtra("uid", chatList.get(position).getUid());
                intentToChat.putExtra("username", chatList.get(position).getUsername());
                intentToChat.putExtra("profile", chatList.get(position).getProfile());
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
        TextView tv_msg;

        public ViewHolder(ImageView imageView, TextView tv_username, TextView tv_msg){
            this.imageView = imageView;
            this.tv_username = tv_username;
            this.tv_msg = tv_msg;

        }
    }
}
