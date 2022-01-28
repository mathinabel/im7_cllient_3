package com.quyuanjin.imseven.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatParce implements Parcelable {
    public Long myid;
    public String myUserId;//实际上是数字userid
    public String yourUserId;//目前是phone
    public String myportrait;//头像
    public String youportrait;//头像

    public ChatParce() {
    }

    public ChatParce(Long myid, String myUserId, String yourUserId, String myportrait, String youportrait) {
        this.myid = myid;
        this.myUserId = myUserId;
        this.yourUserId = yourUserId;
        this.myportrait = myportrait;
        this.youportrait = youportrait;
    }

    public ChatParce(Parcel in) {
        if (in.readByte() == 0) {
            myid = null;
        } else {
            myid = in.readLong();
        }
        myUserId = in.readString();
        yourUserId = in.readString();
        myportrait = in.readString();
        youportrait = in.readString();
    }

    public static final Creator<ChatParce> CREATOR = new Creator<ChatParce>() {
        @Override
        public ChatParce createFromParcel(Parcel in) {
            return new ChatParce(in);
        }

        @Override
        public ChatParce[] newArray(int size) {
            return new ChatParce[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (myid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(myid);
        }
        parcel.writeString(myUserId);
        parcel.writeString(yourUserId);
        parcel.writeString(myportrait);
        parcel.writeString(youportrait);
    }
}
