package com.quyuanjin.imseven.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.quyuanjin.imseven.Constant;
import com.quyuanjin.imseven.MainActivity;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class LoginAndRegisterAc extends AppCompatActivity implements View.OnClickListener {
    private ImageView backgroundImage;
    private EditText phoneEditText;
    private EditText pwd;
    private Button login;
    private Button register;
    private TextView morning;
    private TextView littleTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login_or_register);
        initView();
        initListener();

    }


    private void initListener() {
        backgroundImage.setOnClickListener(this);
        phoneEditText.setOnClickListener(this);
        pwd.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        littleTitle.setOnClickListener(this);
    }

    private void initView() {
        backgroundImage = findViewById(R.id.imageView2);
        phoneEditText = findViewById(R.id.phoneEditText);
        pwd = findViewById(R.id.pwd);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        morning = findViewById(R.id.morning);
        littleTitle = findViewById(R.id.littleTitle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                break;
            case R.id.phoneEditText:
                break;


            case R.id.login:

                String userid = phoneEditText.getText().toString();
                String pwd2 = pwd.getText().toString();

                if (userid.equals("") || "".equals(pwd2)) {
                    MessageDialog.show( this, "提示", "请正确填写内容", "确定");
                } else {
                    WaitDialog.show(this, "请稍候...");
                    OkHttpUtils.post()
                            .url(Constant.URL + "login")
                            .addParams("userid", userid)
                            .addParams("pwd", pwd2)
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Gson gson = new Gson();
                            User user = gson.fromJson(response, User.class);

                            if (!"".equals(user.getPwd())) {
                                SharedPreferencesUtils.setParam(getApplicationContext(), "name", user.getName());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "des", user.getDescription());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "sex", user.getSex());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "portrait", user.getPortrait());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "uuid", user.getToken());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "userid", user.getId());
                                Intent intent = new Intent(LoginAndRegisterAc.this, MainActivity.class);
                                startActivity(intent);
                                WaitDialog.dismiss();
                                finish();
                            } else {
                                ToastUtils.show(getApplicationContext(), "w");
                            }
                        }

                    });
                }
                break;
            case R.id.register:
                WaitDialog.show(this, "请稍候...");

                OkHttpUtils.post()
                        .url(Constant.URLRegister + "register")
                        //      .params( )//
                        //     .headers()//
                        .build()//
                        .execute(
                                new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {

                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        Gson gson = new Gson();
                                        User user = gson.fromJson(response, User.class);
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "name", user.getName());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "des", user.getDescription());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "sex", user.getSex());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "portrait", user.getPortrait());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "uuid", user.getToken());
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "userid", user.getUser());
                                        Intent intent = new Intent(LoginAndRegisterAc.this, MainActivity.class);
                                        startActivity(intent);
                                        WaitDialog.dismiss();

                                        finish();

                                    }


                                    @Override
                                    public void inProgress(float progress, long total, int id) {
                                        super.inProgress(progress, total, id);


                                    }
                                });


                break;
        }
    }

}
