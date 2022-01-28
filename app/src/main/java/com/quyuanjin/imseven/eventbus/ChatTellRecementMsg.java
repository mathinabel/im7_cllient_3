package com.quyuanjin.imseven.eventbus;

import com.quyuanjin.imseven.pojo.Msg;

public class ChatTellRecementMsg {

    private  Msg msg;

    public ChatTellRecementMsg(Msg msg) {
        this.msg = msg;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
