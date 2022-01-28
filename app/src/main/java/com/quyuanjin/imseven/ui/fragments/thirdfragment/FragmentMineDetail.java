package com.quyuanjin.imseven.ui.fragments.thirdfragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.transition.TransitionInflater;

import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.base.BaseMainFragment;

import me.yokeyword.fragmentation.SupportFragment;

public class FragmentMineDetail extends BaseMainFragment {
    public static SupportFragment newInstance() {
        Bundle args = new Bundle();

        FragmentMineDetail fragment = new FragmentMineDetail();
        fragment.setArguments(args);
        return fragment;
    }


    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mine_detail, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }


}
