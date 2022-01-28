package com.quyuanjin.imseven.ui.fragments.mydetailfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.base.BaseMainFragment;
import com.quyuanjin.imseven.ui.fragments.settingfragment.FragmentSetting;

import me.yokeyword.fragmentation.SupportFragment;

public class FragmentMyDetail extends BaseMainFragment {
    public static SupportFragment newInstance() {
        Bundle args = new Bundle();
        FragmentMyDetail fragment = new FragmentMyDetail();
        fragment.setArguments(args);
        return fragment;
    }
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmnet_my_detail, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();

    }

    private void initData() {
    }
}
