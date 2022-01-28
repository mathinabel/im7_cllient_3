package com.quyuanjin.imseven.pojoserver;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity

public class UnReadAddFriendRequest {
    @Id(autoincrement = true)
    Long id;
   private String receiverId;
    private String userid;
    private String name;
    private String portrait;
    private String msg;
    private String yourName;
    private String yourPortrait;
    private String yourMsg;
    private String createTime;
    private String receiverTime;
    private String sendType;
    private String readType;
    private String isAgree;
    @Generated(hash = 1202720704)
    public UnReadAddFriendRequest() {
    }


    @Generated(hash = 1238922469)
    public UnReadAddFriendRequest(Long id, String receiverId, String userid, String name, String portrait, String msg, String yourName, String yourPortrait, String yourMsg, String createTime, String receiverTime, String sendType, String readType, String isAgree) {
        this.id = id;
        this.receiverId = receiverId;
        this.userid = userid;
        this.name = name;
        this.portrait = portrait;
        this.msg = msg;
        this.yourName = yourName;
        this.yourPortrait = yourPortrait;
        this.yourMsg = yourMsg;
        this.createTime = createTime;
        this.receiverTime = receiverTime;
        this.sendType = sendType;
        this.readType = readType;
        this.isAgree = isAgree;
    }


    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    public String getYourPortrait() {
        return yourPortrait;
    }

    public void setYourPortrait(String yourPortrait) {
        this.yourPortrait = yourPortrait;
    }

    public String getYourMsg() {
        return yourMsg;
    }

    public void setYourMsg(String yourMsg) {
        this.yourMsg = yourMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
    public String getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(String isAgree) {
        this.isAgree = isAgree;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReceiverTime() {
        return receiverTime;
    }

    public void setReceiverTime(String receiverTime) {
        this.receiverTime = receiverTime;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }
}
