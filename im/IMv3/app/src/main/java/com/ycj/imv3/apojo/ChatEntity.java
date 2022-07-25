package com.ycj.imv3.apojo;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class ChatEntity implements Parcelable{
    private int type;
    private boolean isSelf;
    private String name;
    private String profile;
    private String data;

    public ChatEntity(int type, boolean isSelf, String name, String profile, String data) {
        this.type = type;
        this.isSelf = isSelf;
        this.name = name;
        this.profile = profile;
        this.data = data;
    }

    protected ChatEntity(Parcel in) {
        type = in.readInt();
        isSelf = in.readByte() != 0;
        name = in.readString();
        profile = in.readString();
        data = in.readString();
    }

    public static final Creator<ChatEntity> CREATOR = new Creator<ChatEntity>() {
        @Override
        public ChatEntity createFromParcel(Parcel in) {
            return new ChatEntity(in);
        }

        @Override
        public ChatEntity[] newArray(int size) {
            return new ChatEntity[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeBoolean(isSelf);
        dest.writeString(name);
        dest.writeString(profile);
        dest.writeString(data);
    }
}
