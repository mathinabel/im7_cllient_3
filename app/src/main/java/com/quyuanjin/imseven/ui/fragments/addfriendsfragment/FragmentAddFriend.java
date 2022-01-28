package com.quyuanjin.imseven.ui.fragments.addfriendsfragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.quyuanjin.imseven.App;
import com.quyuanjin.imseven.Constant;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.pojo.AllTypeMsg;
import com.quyuanjin.imseven.pojo.ChatParce;
import com.quyuanjin.imseven.pojoserver.UnReadAddFriendRequest;
import com.quyuanjin.imseven.ui.activity.login.User;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.TimeHelper;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;

public class FragmentAddFriend extends SupportFragment {
    private static final String ARG_ONE = "arg_one";
    private ChatParce chatParce;
    private View view;

    private Button btnAddFriend;
    private TextView tvName;
    private TextView tvAccount5;
    private SimpleDraweeView ivHeader;

    private String userid;
    private String portrait;
    private String name;
    public static FragmentAddFriend newInstance(ChatParce msg) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ONE, msg);
        FragmentAddFriend fragment = new FragmentAddFriend();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatParce = getArguments().getParcelable(ARG_ONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_friend, container, false);
        userid = (String) SharedPreferencesUtils.getParam(_mActivity, "userid", "");
        portrait = (String) SharedPreferencesUtils.getParam(_mActivity, "portrait", "");
        name = (String) SharedPreferencesUtils.getParam(_mActivity, "name", "");

        initView();
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initDataFromNet();
    }

    private User user;

    private void initDataFromNet() {
        ToastUtils.show(_mActivity, "kaishile" + chatParce.yourUserId);
        OkHttpUtils.post()
                .url(Constant.URL_save + "PullUserDetailByOne")
                .addParams("phone", chatParce.yourUserId)//目前是phone
                .addParams("name", chatParce.myportrait)//目前是name

                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ToastUtils.show(_mActivity,name);
                        Gson gson = new Gson();
                        user = gson.fromJson(response, User.class);
                        if (!"".equals(user.getPhone())) {
                            tvAccount5.setText(user.getPhone());
                            tvName.setText(user.getName());
                            if (user.getPortrait() != null) {
                                ivHeader.setImageURI(Uri.parse(user.getPortrait()));
                            } else {

                            }
                            initListener(user, name);
                        }


                        //     }

                        //    }).start();

                    }


                });
    }

    private void initListener(User user, String name) {
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                UnReadAddFriendRequest unReadAddFriendRequest = new UnReadAddFriendRequest(
                        null, user.getUser(), userid,name,portrait,"",user.getName(),user.getPortrait(), "", TimeHelper.getCurrentTime(), "", Constant.already_send, Constant.un_read, ""
                );
                AllTypeMsg allTypeMsg=new AllTypeMsg(unReadAddFriendRequest, null,
                        null, null,
                        Constant.unReadAddFriendRequest_json);
                String s = gson.toJson(allTypeMsg);
                //将目标信息 发送
                OkHttpUtils.post()
                        .url(Constant.URL_userdetail + "addfriend")
                        .addParams("userid", user.getUser())
                        .addParams("contant", s)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (response.equals("ok")) {
                                    ToastUtils.show(_mActivity, "ok");
                                    App.getDaoSession().getUnReadAddFriendRequestDao().insert(unReadAddFriendRequest);
                                } else {
                                    ToastUtils.show(_mActivity, "not ok");

                                }
                            }
                        });

            }
        });
    }

    private void initView() {
        ivHeader = view.findViewById(R.id.ivHeader1);
        btnAddFriend = view.findViewById(R.id.btnAddFriend);
        tvName = view.findViewById(R.id.tvName5);
        tvAccount5 = view.findViewById(R.id.tvAccount5);
    }

}
