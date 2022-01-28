package com.quyuanjin.imseven.ui.fragments.addfragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.quyuanjin.imseven.Constant;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.pojo.ChatParce;
import com.quyuanjin.imseven.ui.activity.login.User;
import com.quyuanjin.imseven.ui.fragments.addfriendsfragment.FragmentAddFriend;
import com.quyuanjin.imseven.ui.fragments.chatfragment.FragmentChat;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;

public class FragmentAdd extends SupportFragment implements View.OnClickListener {
    private EditText searchSomething;
    private TextView search_text_view;

    private LinearLayout search_result;
    private ListView addFriendAcLV;
    private Button cancel;


    private View view;

    private static final String ARG_ONE = "arg_one";
    private ChatParce chatParce;
    private String userid;

    public static FragmentAdd newInstance(/*ChatParce msg*/) {
        Bundle args = new Bundle();
        //  args.putParcelable(ARG_ONE, msg);
        FragmentAdd fragment = new FragmentAdd();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  chatParce = getArguments().getParcelable(ARG_ONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);
        initView(view);
        userid = (String) SharedPreferencesUtils.getParam(_mActivity, "userid", "");

        return view;
    }

    private void initView(View view) {
        searchSomething = view.findViewById(R.id.search_something);
        search_result = view.findViewById(R.id.search_result);
        search_text_view = view.findViewById(R.id.keywordTextView);
        addFriendAcLV = view.findViewById(R.id.addFriendAcLV);
        cancel = view.findViewById(R.id.cancel);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initListener();
    }

    private void initListener() {
        cancel.setOnClickListener(this);
        searchSomething.setOnClickListener(this);
        searchSomething.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    addFriendAcLV.setVisibility(View.VISIBLE);
                    search_result.setVisibility(View.VISIBLE);
                    search_result.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            WaitDialog.show(_mActivity, "请稍候...");

                            OkHttpUtils.get()
                                    .url(Constant.URL_userdetail + "PullUserDetail")

                                    .addParams("phone", editable.toString())
                                    .build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    WaitDialog.dismiss();
                                    TipDialog.show(_mActivity, "查询失败", TipDialog.TYPE.WARNING);
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    WaitDialog.show(_mActivity, "请稍候...");
                                    if (response.equals("0")) {
                                        WaitDialog.dismiss();
                                        MessageDialog.show(_mActivity, "提示", "没找到该号码所对应的用户", "确定");
                                    } else {


                                        Gson gson1 = new Gson();
                                        List<User> list = gson1.fromJson(response, new TypeToken<List<User>>() {
                                        }.getType());
                                        if (list.size() > 1) {
                                            List strings = new ArrayList();
                                            for (int i = 0; i < list.size(); i++) {
                                                strings.add( list.get(i).getName());
                                            }
                                            BottomMenu.show(_mActivity, strings, new OnMenuItemClickListener() {
                                                @Override
                                                public void onClick(String text, int index) {
                                                    //返回参数 text 即菜单名称，index 即菜单索引
                                                    ChatParce parce = new ChatParce(null,
                                                            userid, list.get(index).getPhone(), text, null);
                                                    start(FragmentAddFriend.newInstance(parce));
                                                }
                                            });
                                        } else {
                                            ChatParce parce = new ChatParce(null,
                                                    userid, editable.toString(), null, null);
                                            start(FragmentAddFriend.newInstance(parce));
                                        }

                                        ToastUtils.show(_mActivity, "net发送成功");

                                        WaitDialog.dismiss();
                                    }
                                }


                            });


                        }
                    });
                    search_text_view.setText(searchSomething.getText());
                } else {
                    addFriendAcLV.setVisibility(View.GONE);
                    search_result.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_something:


                break;
            case R.id.cancel:
                _mActivity.onBackPressed();
                break;

        }
    }
}
