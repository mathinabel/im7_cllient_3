package com.quyuanjin.imseven.ui.fragments.newfriendfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.quyuanjin.imseven.App;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.adapter.NewFriendAdapter;
import com.quyuanjin.imseven.base.BaseBackFragment;
import com.quyuanjin.imseven.pojo.PojoContract;
import com.quyuanjin.imseven.pojoserver.UnReadAddFriendRequest;
import com.quyuanjin.imseven.pojoserver.UnReadAddFriendRequestDao;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

public class FragmentNewFriend extends BaseBackFragment {
    public static SupportFragment newInstance() {
        Bundle args = new Bundle();

        FragmentNewFriend fragment = new FragmentNewFriend();
        fragment.setArguments(args);
        return fragment;
    }

    private View view;
    private NewFriendAdapter newFriendAdapter;
    private SwipeRecyclerView rvNewFriend;
    private String userid;
    private ArrayList<UnReadAddFriendRequest> entityList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_new_friend, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        userid = (String) SharedPreferencesUtils.getParam(_mActivity, "userid", "");

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initBackListener();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        rvNewFriend = view.findViewById(R.id.rvNewFriend);
        newFriendAdapter = new NewFriendAdapter(entityList, _mActivity);
        rvNewFriend.setLayoutManager(new LinearLayoutManager(_mActivity));
        rvNewFriend.setAdapter(newFriendAdapter);
        newFriendAdapter.notifyDataSetChanged();
    }

    private void initData() {
        List<UnReadAddFriendRequest> unReadAddFriendRequestList = App.getDaoSession().getUnReadAddFriendRequestDao().loadAll();
        if (unReadAddFriendRequestList.size() > 0) {
            entityList.addAll(unReadAddFriendRequestList);
        }
    }

    private void initBackListener() {
        CommonTitleBar commonTitleBar = view.findViewById(R.id.titlebar6);
        commonTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    _mActivity.onBackPressed();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void UnReadAddFriendFragmentNewFriend(PojoContract pojoContract) {
        // Log.d("looaa","fragmentmine收到了");
        entityList.clear();
        initData();
        newFriendAdapter.notifyDataSetChanged();
    }
}
