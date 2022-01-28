package com.quyuanjin.imseven.netty;


import android.util.Log;

import com.google.gson.Gson;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.quyuanjin.imseven.App;
import com.quyuanjin.imseven.Constant;
import com.quyuanjin.imseven.eventbus.MsgBus;
import com.quyuanjin.imseven.eventbus.MsgBusTo;
import com.quyuanjin.imseven.pojo.AllTypeMsg;
import com.quyuanjin.imseven.pojo.Msg;
import com.quyuanjin.imseven.pojo.PojoContract;
import com.quyuanjin.imseven.pojo.PojoRecementMsg;
import com.quyuanjin.imseven.pojoserver.UnReadAddFriendRequest;
import com.quyuanjin.imseven.utils.TimeHelper;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.greenrobot.eventbus.EventBus;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import okhttp3.Call;

public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    public static Msg changeMsg(Msg msg) {
        Msg msg1 = new Msg();
        switch (msg.getContentType()) {
            case Constant.item_text_send:
                msg1 = new Msg(null, msg.getSendID(), msg.getReceiveId(), msg.getMsg(), msg.getMsgUUID(),
                        msg.getCreateTime(), msg.getLocalPath(), msg.getNetPath(), Constant.item_text_receive,
                        msg.getRecorderTime(), msg.getSendSucceedType(), msg.getReadType(),
                        msg.getFanyi());
              //  Log.d("669",msg1.getMsg());
                App.getDaoSession().getMsgDao().insert(msg1);
             //   Log.d("669","执行了appinsert");

                break;
            case Constant.item_audio_send:
                msg1 = new Msg(null, msg.getSendID(), msg.getReceiveId(), msg.getMsg(), msg.getMsgUUID(),
                        msg.getCreateTime(), msg.getLocalPath(), msg.getNetPath(), Constant.item_audio_receive,
                        msg.getRecorderTime(), msg.getSendSucceedType(), msg.getReadType(),
                        msg.getFanyi());
                App.getDaoSession().getMsgDao().insert(msg1);
                break;
            case Constant.item_image_send:
                msg1 = new Msg(null, msg.getSendID(), msg.getReceiveId(), msg.getMsg(), msg.getMsgUUID(),
                        msg.getCreateTime(), msg.getLocalPath(), msg.getNetPath(), Constant.item_image_receive,
                        msg.getRecorderTime(), msg.getSendSucceedType(), msg.getReadType(),
                        msg.getFanyi());


                App.getDaoSession().getMsgDao().insert(msg1);
                break;
            case Constant.item_video_send:
                msg1 = new Msg(null, msg.getSendID(), msg.getReceiveId(), msg.getMsg(), msg.getMsgUUID(),
                        msg.getCreateTime(), msg.getLocalPath(), msg.getNetPath(), Constant.item_video_receive,
                        msg.getRecorderTime(), msg.getSendSucceedType(), msg.getReadType(),
                        msg.getFanyi());
                App.getDaoSession().getMsgDao().insert(msg1);
                break;
            case Constant.item_location_send:
                msg1 = new Msg(null, msg.getSendID(), msg.getReceiveId(), msg.getMsg(), msg.getMsgUUID(),
                        msg.getCreateTime(), msg.getLocalPath(), msg.getNetPath(), Constant.item_location_receive,
                        msg.getRecorderTime(), msg.getSendSucceedType(), msg.getReadType(),
                        msg.getFanyi());
                App.getDaoSession().getMsgDao().insert(msg1);
                break;
        }
        return msg1;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        if (!msg.contains("HTTP/1.1") && !msg.contains("Host:") && !msg.contains("Proxy-Connection: keep-alive") && !msg.contains("User2Model-Agent:")) {
            System.out.println(msg);
            Gson gson = new Gson();
            AllTypeMsg allTypeMsg = gson.fromJson(msg, AllTypeMsg.class);
            //recemsg1是各种对应的json
            switch (allTypeMsg.getJsonTpye()) {
                //应对消息类型存进不同的表然后进行  event bus
                // 和通知服务端数据库已经收到消息，更改发送状态
                case Constant.msg_json:
                    Msg msg1 = allTypeMsg.getMsg();
                    if (!msg1.getSendID().equals("")){

                        EventBus.getDefault().post(new MsgBusTo(msg1));// 通知消息界面
                        //没改的数据给消息界面，改了的数据保存并给chat
                        EventBus.getDefault().post(new MsgBus( changeMsg(msg1)));//通知正在聊天界面

                        OkHttpUtils.get()
                                .url(Constant.URL_msg_send + "changeMsgState")
                                .addParams("msguuid", msg1.getMsgUUID())
                                .build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (response.equals("ok")) {
                                }
                            }
                        });

                    }
                    break;

                case Constant.pojocontract_json:
                    PojoContract pojoContract = allTypeMsg.getPojoContract();

                    break;
                case Constant.pojorecementmsg_json:
                    PojoRecementMsg pojoRecementMsg = allTypeMsg.getPojoRecementMsg();

                    break;

                case Constant.unReadAddFriendRequest_json:
                    UnReadAddFriendRequest u = allTypeMsg.getUnReadAddFriendRequest();
//todo 头像资源转储本地，并且更改  u.setYourPortrait();
                    //先本地存库，再通知mainfragment和第三fragment做出相应的变化
                    if ("".equals(u.getIsAgree())) {
                       //我收到加好友请求了
                        App.getDaoSession().getUnReadAddFriendRequestDao().insert(u);
                        //通知mainFragment修改下标和Fragmentmine的红点
                        EventBus.getDefault().post(u);
                    } else if (Constant.addFriend_answer_agree.equals(u.getIsAgree())) {
                   // 我的加好友请求对方同意了
                        //添加数据库
                        PojoContract pojoContract1 =new PojoContract(null,
                                u.getYourPortrait(),u.getYourName(),"","",u.getReceiverId(),
                                Constant.TAG_ITEM,"",u.getUserid(),u.getReceiverId());

                        PojoRecementMsg pojoRecementMsg1=new PojoRecementMsg(
                                null,u.getUserid(),u.getReceiverId(),u.getYourPortrait(),
                                "",u.getYourName(), TimeHelper.getCurrentTime(),"",
                                "","你好啊",Constant.slient_false
                        );
                        App.getDaoSession().getPojoContractDao().insert(pojoContract1);
                        App.getDaoSession().getPojoRecementMsgDao().insert(pojoRecementMsg1);
                       App.getDaoSession().getUnReadAddFriendRequestDao().update(u);
                        //通知相对应的界面更新数据

                        EventBus.getDefault().post(pojoRecementMsg1);//联系人界面
                        EventBus.getDefault().post(pojoContract1);//消息界面
                        EventBus.getDefault().post(pojoContract1);//新好友请求界面

                    }else {

                    }

                    break;
                default:
                    break;

            }

        }


    }
}