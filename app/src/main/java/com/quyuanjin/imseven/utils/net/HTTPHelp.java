package com.quyuanjin.imseven.utils.net;

import com.quyuanjin.imseven.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

public class HTTPHelp {
    public RequestCall login(String gson) {
        return OkHttpUtils.post()
                .url(Constant.URL + "sendMsg")
                .addParams("token","token")
                .addParams("msg",gson)
                .build();
    }
    public RequestCall register(String gson) {
        return OkHttpUtils.post()
                .url(Constant.URL + "sendMsg")
                .addParams("token","token")
                .addParams("msg",gson)
                .build();
    }
    public RequestCall sendMsg(String gson) {
        return OkHttpUtils.post()
                .url(Constant.URL + "sendMsg")
                .addParams("token","token")
                .addParams("msg",gson)
                .build();
    }

    public RequestCall addFriend(String gson) {
        return OkHttpUtils.post()
                .url(Constant.URL + "sendMsg")
                .addParams("token","token")
                .addParams("msg",gson)
                .build();
    }
    public RequestCall pullContract(String gson) {
        return OkHttpUtils.post()
                .url(Constant.URL + "sendMsg")
                .addParams("token","token")
                .addParams("msg",gson)
                .build();
    }
    public RequestCall pullUnReadMsg(String gson) {
        return OkHttpUtils.post()
                .url(Constant.URL + "sendMsg")
                .addParams("token","token")
                .addParams("msg",gson)
                .build();
    }
    public RequestCall pullFriendDetailInformation(String gson) {
        return OkHttpUtils.post()
                .url(Constant.URL + "sendMsg")
                .addParams("token","token")
                .addParams("msg",gson)
                .build();
    }

}
