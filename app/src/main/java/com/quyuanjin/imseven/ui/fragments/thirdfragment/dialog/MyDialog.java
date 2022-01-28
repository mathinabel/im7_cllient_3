package com.quyuanjin.imseven.ui.fragments.thirdfragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.yzq.zxinglibrary.encode.CodeCreator;

public class MyDialog extends Dialog implements View.OnClickListener {
    private Context context;
    String phone;

    public MyDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_show_image);
        //设置点击布局外则Dialog消失
        setCanceledOnTouchOutside(true);

        phone = (String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "userid", "");

    }

    public void showDialog() {
        Window window = getWindow();
        //设置弹窗动画
        window.setWindowAnimations(R.style.style_dialog);
        //设置Dialog背景色
        window.setBackgroundDrawableResource(R.color.assist_blue);
        WindowManager.LayoutParams wl = window.getAttributes();
        //设置弹窗位置
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);


        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap = CodeCreator.createQRCode(phone, 900, 900, logo);


        ImageView imageView = findViewById(R.id.iv_image);


        imageView.setImageBitmap(bitmap);
        findViewById(R.id.iv_image).setOnClickListener(this);


        show();


    }


    @Override
    public void onClick(View view) {
        dismiss();
    }
}
