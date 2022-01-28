package com.quyuanjin.imseven.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.quyuanjin.imseven.App;
import com.quyuanjin.imseven.Constant;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.pojo.AllTypeMsg;
import com.quyuanjin.imseven.pojo.PojoContract;
import com.quyuanjin.imseven.pojo.PojoRecementMsg;
import com.quyuanjin.imseven.pojoserver.UnReadAddFriendRequest;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.TimeHelper;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.ViewHolder> {

    private ArrayList<UnReadAddFriendRequest> entityList;
    private Context context;
    String name;
    String des;
    String sex;
    String por;
    String userid;

    public NewFriendAdapter(ArrayList<UnReadAddFriendRequest> entityList, Context context) {
        this.entityList = entityList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        name = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "name", "");
        des = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "des", "");
        sex = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "sex", "");
        por = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "portrait", "");
        userid = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "userid", "");


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_friend_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (entityList.size() != 0) {

            UnReadAddFriendRequest unReadAddFriendRequest = entityList.get(position);

            holder.nameTextView.setText(unReadAddFriendRequest.getName());
//            if (!"".equals(unReadAddFriendRequest.getPortrait())){
//                holder.portraitImageView3.setImageURI(Uri.parse(unReadAddFriendRequest.getPortrait()));
//            }
            if (userid.equals(entityList.get(position).getUserid())
                    && (!Constant.addFriend_answer_agree.equals(entityList.get(position).getIsAgree()))) {
//自己发的已发送
                holder.acceptButton.setVisibility(View.GONE);
                holder.acceptStatusTextView.setVisibility(View.VISIBLE);
                holder.acceptStatusTextView.setText("待确认");
            } else if ((userid.equals(entityList.get(position).getReceiverId()))
                    && (!Constant.addFriend_answer_agree.equals(entityList.get(position).getIsAgree()))) {//我是接收者
//别人发的，等待确认
                holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.acceptButton.setVisibility(View.GONE);
                        holder.acceptStatusTextView.setVisibility(View.VISIBLE);
                        //我同意了加好友请求，联系人数据库加一，最近联系人数据库加一
                        //添加数据库
                        UnReadAddFriendRequest un=entityList.get(position);
                        PojoContract pojoContract =new PojoContract(null,
                                un.getPortrait(),un.getName(),"","",un.getUserid(),
                                Constant.TAG_ITEM,"","","");

                        PojoRecementMsg pojoRecementMsg=new PojoRecementMsg(
                                null,userid,un.getUserid(),un.getPortrait(),
                                "",un.getName(),TimeHelper.getCurrentTime(),"",
                                "","你好啊",Constant.slient_false
                        );
                        App.getDaoSession().getPojoContractDao().insert(pojoContract);
                        App.getDaoSession().getPojoRecementMsgDao().insert(pojoRecementMsg);
                        //通知相对应的界面更新数据
                        EventBus.getDefault().post(pojoRecementMsg);
                        EventBus.getDefault().post(pojoContract);

                        //发送网络更新好友请求状态

                        Gson gson = new Gson();
                        unReadAddFriendRequest.setIsAgree(Constant.addFriend_answer_agree);
                        AllTypeMsg allTypeMsg = new AllTypeMsg(unReadAddFriendRequest, null,
                                null, null,
                                Constant.unReadAddFriendRequest_json);
                        String s = gson.toJson(allTypeMsg);
                        OkHttpUtils.post()
                                .url(Constant.URL_userdetail + "addFriendAnswer")
                                .addParams("userid", unReadAddFriendRequest.getUserid())
                                .addParams("contant", s)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {

                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        if (response.equals("ok")) {
                                            App.getDaoSession().getUnReadAddFriendRequestDao().update(un);
                                            //     ToastUtils.show(_mActivity, "ok");
                                        } else {
                                            //       ToastUtils.show(_mActivity, "not ok");

                                        }
                                    }
                                });
                    }
                });
            } else {
                holder.acceptButton.setVisibility(View.GONE);
                holder.acceptStatusTextView.setVisibility(View.VISIBLE);
                holder.acceptStatusTextView.setText("已同意");
            }


        }
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView portraitImageView3;
        TextView nameTextView;
        Button acceptButton;
        TextView acceptStatusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            portraitImageView3 = itemView.findViewById(R.id.portraitImageView3);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            acceptStatusTextView = itemView.findViewById(R.id.acceptStatusTextView);

        }
    }
}

