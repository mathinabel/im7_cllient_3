package com.quyuanjin.imseven.eventbus;

import com.quyuanjin.imseven.pojo.Msg;

public class MsgBus {
    private Msg msg;

    public MsgBus(Msg msg) {
        this.msg = msg;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
