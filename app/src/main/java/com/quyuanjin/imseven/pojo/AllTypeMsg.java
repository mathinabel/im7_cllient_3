package com.quyuanjin.imseven.pojo;

import com.quyuanjin.imseven.pojoserver.UnReadAddFriendRequest;

public class AllTypeMsg {
    private UnReadAddFriendRequest unReadAddFriendRequest;
    private PojoContract pojoContract;
    private PojoRecementMsg pojoRecementMsg;
    private Msg msg;
    private String jsonTpye;

    public AllTypeMsg(UnReadAddFriendRequest unReadAddFriendRequest, PojoContract pojoContract, PojoRecementMsg pojoRecementMsg, Msg msg, String jsonTpye) {
        this.unReadAddFriendRequest = unReadAddFriendRequest;
        this.pojoContract = pojoContract;
        this.pojoRecementMsg = pojoRecementMsg;
        this.msg = msg;
        this.jsonTpye = jsonTpye;
    }

    public UnReadAddFriendRequest getUnReadAddFriendRequest() {
        return unReadAddFriendRequest;
    }

    public void setUnReadAddFriendRequest(UnReadAddFriendRequest unReadAddFriendRequest) {
        this.unReadAddFriendRequest = unReadAddFriendRequest;
    }

    public AllTypeMsg() {
    }

    public PojoContract getPojoContract() {
        return pojoContract;
    }

    public void setPojoContract(PojoContract pojoContract) {
        this.pojoContract = pojoContract;
    }

    public PojoRecementMsg getPojoRecementMsg() {
        return pojoRecementMsg;
    }

    public void setPojoRecementMsg(PojoRecementMsg pojoRecementMsg) {
        this.pojoRecementMsg = pojoRecementMsg;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public String getJsonTpye() {
        return jsonTpye;
    }

    public void setJsonTpye(String jsonTpye) {
        this.jsonTpye = jsonTpye;
    }
}
