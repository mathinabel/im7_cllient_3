package com.quyuanjin.imseven.ui.fragments.settingfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.lqr.optionitemview.OptionItemView;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.base.BaseMainFragment;
import com.quyuanjin.imseven.ui.fragments.secondfragment.FragmentContract;
import com.quyuanjin.imseven.ui.fragments.settingfragment.settingdetailfrag.FragmentCompleteinfo;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import me.yokeyword.fragmentation.SupportFragment;

public class FragmentSetting extends BaseMainFragment {
    public static SupportFragment newInstance() {
        Bundle args = new Bundle();
        FragmentSetting fragment = new FragmentSetting();
        fragment.setArguments(args);
        return fragment;
    }
    private View view;
    private OptionItemView completeinfo;
    private CommonTitleBar commonTitleBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmnet_setting, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        completeinfo=view.findViewById(R.id.completeinfo);
        commonTitleBar = view.findViewById(R.id.titlebar10);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initClick();
        initData();

    }

    private void initClick() {
        completeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(FragmentCompleteinfo.newInstance());
            }
        });

        commonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    _mActivity.onBackPressed();
                }
            }
        });
    }

    private void initData() {
    }
}
