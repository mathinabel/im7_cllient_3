package com.quyuanjin.imseven.eventbus;

import com.quyuanjin.imseven.pojo.Msg;

public class MsgBusTo {
    private Msg msg;

    public MsgBusTo(Msg msg) {
        this.msg = msg;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
