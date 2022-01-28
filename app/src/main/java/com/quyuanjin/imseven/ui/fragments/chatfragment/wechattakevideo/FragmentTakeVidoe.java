package com.quyuanjin.imseven.ui.fragments.chatfragment.wechattakevideo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.base.BaseBackFragment;
import com.quyuanjin.imseven.eventbus.BitMapUrl;
import com.quyuanjin.imseven.eventbus.UrlMsg;
import com.quyuanjin.imseven.ui.fragments.secondfragment.FragmentContract;
import com.quyuanjin.imseven.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import me.yokeyword.fragmentation.SupportFragment;

public class FragmentTakeVidoe extends BaseBackFragment {
    public static FragmentTakeVidoe newInstance() {
        Bundle args = new Bundle();
        FragmentTakeVidoe fragment = new FragmentTakeVidoe();
        fragment.setArguments(args);
        return fragment;
    }
    private View view;
    private JCameraView jCameraView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.camera_view_layout, container, false);
        initView(view);
        return  view ;
    }

    private void initView(View view) {
        jCameraView =  view. findViewById(R.id.jcameraview);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        wechatCamera();
    }

    private void wechatCamera(){
//设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

//设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);

//设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);

//JCameraView监听
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {

            }

            @Override
            public void AudioPermissionError() {

            }
        });

        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
               // EventBus.getDefault().post(new BitMapUrl(bitmap));
                _mActivity.onBackPressed();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
              //  ToastUtils.show(_mActivity,url);
                EventBus.getDefault().post(new UrlMsg(url));
                _mActivity.onBackPressed();
            }
        });
//左边按钮点击事件
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {

            }
        });
//右边按钮点击事件
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {


                Toast.makeText(_mActivity,"Right",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
       jCameraView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        jCameraView.onPause();
    }


}
