package com.quyuanjin.imseven.ui.fragments.settingfragment.settingdetailfrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.lqr.optionitemview.OptionItemView;
import com.quyuanjin.imseven.Constant;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.base.BaseBackFragment;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;
import okhttp3.Response;

public class FragmentCompleteinfo extends BaseBackFragment {
    public static SupportFragment newInstance() {
        Bundle args = new Bundle();
        FragmentCompleteinfo fragment = new FragmentCompleteinfo();
        fragment.setArguments(args);
        return fragment;
    }

    private View view;
    private String userid;
    private CommonTitleBar commonTitleBar;
    private OptionItemView change_phone;
    private OptionItemView change_pwd;
    private OptionItemView change_portrait;
    private OptionItemView change_persionality;
    private OptionItemView change_nickname;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmnet_setting_complinfo, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        userid = (String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "userid", "");

        commonTitleBar = view.findViewById(R.id.titlebar11);
        change_phone = view.findViewById(R.id.change_phone);
        change_pwd = view.findViewById(R.id.change_pwd);
        change_portrait = view.findViewById(R.id.change_portrait);
        change_persionality = view.findViewById(R.id.change_persionality);
        change_nickname = view.findViewById(R.id.change_nickname);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initClick();
        initData();

    }

    private void initClick() {
        commonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    _mActivity.onBackPressed();
                }
            }
        });
        change_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog.show(_mActivity, "提示", "请输入手机号", "确定", "取消")
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                //inputStr 即当前输入的文本
                                WaitDialog.show(_mActivity, "请稍候...");

                                OkHttpUtils.get()
                                        .url(Constant.URL_save + "changePhone")
                                        .addParams("userid", userid)
                                        .addParams("phone", inputStr)
                                        .build().execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        WaitDialog.dismiss();
                                        TipDialog.show(_mActivity, "更改失败", TipDialog.TYPE.WARNING);
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        if (response.equals("ok")) {
                                            ToastUtils.show(_mActivity, "net发送成功");
                                            SharedPreferencesUtils.setParam(getContext().getApplicationContext(), "phone", inputStr );
                                            WaitDialog.dismiss();
                                        }
                                    }


                                });
                                return false;
                            }
                        });
            }
        });
        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog.show(_mActivity, "提示", "请输入密码", "确定", "取消")
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                //inputStr 即当前输入的文本
                                WaitDialog.show(_mActivity, "请稍候...");

                                OkHttpUtils.get()
                                        .url(Constant.URL_save + "changePwd")
                                        .addParams("userid", userid)
                                        .addParams("pwd", inputStr)
                                        .build().execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        WaitDialog.dismiss();
                                        TipDialog.show(_mActivity, "更改失败", TipDialog.TYPE.WARNING);

                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        if (response.equals("ok")) {
                                            ToastUtils.show(_mActivity, "net发送成功");
                                            SharedPreferencesUtils.setParam(getContext().getApplicationContext(), "pwd", inputStr );
                                            WaitDialog.dismiss();
                                        }
                                    }
                                });
                                return false;
                            }
                        });
            }
        });
        change_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        change_persionality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog.show(_mActivity, "提示", "请输入个性签名", "确定", "取消")
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                WaitDialog.show(_mActivity, "请稍候...");

                                //inputStr 即当前输入的文本
                                OkHttpUtils.get()
                                        .url(Constant.URL_save + "changePer")
                                        .addParams("userid", userid)
                                        .addParams("per", inputStr)
                                        .build().execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        WaitDialog.dismiss();
                                        TipDialog.show(_mActivity, "更改失败", TipDialog.TYPE.WARNING);

                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        if (response.equals("ok")) {
                                            ToastUtils.show(_mActivity, "net发送成功");
                                            SharedPreferencesUtils.setParam(getContext().getApplicationContext(), "des", inputStr );
                                            WaitDialog.dismiss();
                                        }
                                    }
                                });
                                return false;
                            }
                        });
            }
        });
        change_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputDialog.show(_mActivity, "提示", "请输入昵称", "确定", "取消")
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                WaitDialog.show(_mActivity, "请稍候...");

                                //inputStr 即当前输入的文本
                                OkHttpUtils.get()
                                        .url(Constant.URL_save + "changeNickName")
                                        .addParams("userid", userid)
                                        .addParams("nickname", inputStr)
                                        .build().execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        WaitDialog.dismiss();
                                        TipDialog.show(_mActivity, "更改失败", TipDialog.TYPE.WARNING);

                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        if (response.equals("ok")) {
                                            ToastUtils.show(_mActivity, "net发送成功");
                                            SharedPreferencesUtils.setParam(getContext().getApplicationContext(), "name", inputStr );
                                            WaitDialog.dismiss();
                                        }
                                    }
                                });
                                return false;
                            }
                        });
            }
        });

    }

    private void initData() {
    }
}
