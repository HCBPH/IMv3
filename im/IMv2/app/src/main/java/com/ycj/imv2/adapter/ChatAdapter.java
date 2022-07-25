package com.ycj.imv2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ycj.entity.ChatEntity;
import com.ycj.imv2.R;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter implements Serializable {

    private Context context;
    private ArrayList<ChatEntity> data;

    public ArrayList<ChatEntity> getData() {
        return data;
    }

    public void setData(ArrayList<ChatEntity> data) {
        this.data = data;
    }

    public ChatAdapter(Context context, ArrayList<ChatEntity> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder;
        ChatEntity entity = data.get(position);
        boolean isSelf = entity.isSelf();
        int type = entity.getType();

        if (isSelf){
            view = View.inflate(context, R.layout.list_right, null);
            viewHolder = new ViewHolder(view.findViewById(R.id.right_name), view.findViewById(R.id.right_tv), view.findViewById(R.id.right_profile), view.findViewById(R.id.right_img));
        }else{
            view = View.inflate(context, R.layout.list_left, null);
            viewHolder = new ViewHolder(view.findViewById(R.id.left_name), view.findViewById(R.id.left_tv), view.findViewById(R.id.left_profile), view.findViewById(R.id.left_img));
        }
        view.setTag(viewHolder);

        viewHolder.iv_profile.setImageResource(R.drawable.profile);
        if (type == 0){
            viewHolder.tv_name.setText(entity.getName());
            viewHolder.tv_msg.setText(entity.getData());
            viewHolder.iv_picture.setVisibility(View.GONE);
        }else if(type == 1){
            String url = "http://www.ycj.asia:8080/im/img/"+entity.getData();
            Log.d("adapter|glide", url);
            viewHolder.tv_name.setText(entity.getName());
            Glide.with(context).load(url).into(viewHolder.iv_picture);
            viewHolder.tv_msg.setVisibility(View.GONE);
        }
        return view;
    }

    private class ViewHolder{
        TextView tv_msg;
        TextView tv_name;
        ImageView iv_profile;
        ImageView iv_picture;

        ViewHolder(TextView name, TextView msg, ImageView profile, ImageView pic){
            this.tv_name = name;
            this.tv_msg = msg;
            this.iv_profile = profile;
            this.iv_picture = pic;
        }
    }
}
