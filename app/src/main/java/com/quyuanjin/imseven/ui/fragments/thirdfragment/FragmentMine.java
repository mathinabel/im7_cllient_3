package com.quyuanjin.imseven.ui.fragments.thirdfragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quyuanjin.imseven.MainFragment;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.base.BaseMainFragment;
import com.quyuanjin.imseven.pojoserver.UnReadAddFriendRequest;
import com.quyuanjin.imseven.ui.fragments.addfragment.FragmentAdd;
import com.quyuanjin.imseven.ui.fragments.chatfragment.FragmentChat;
import com.quyuanjin.imseven.ui.fragments.detailfragment.FragmentDetail;
import com.quyuanjin.imseven.ui.fragments.firstfragment.serachframgnet.FragmentSearchWorld;
import com.quyuanjin.imseven.ui.fragments.momofragment.FragmentMomo;
import com.quyuanjin.imseven.ui.fragments.newfriendfragment.FragmentNewFriend;
import com.quyuanjin.imseven.ui.fragments.settingfragment.FragmentSetting;
import com.quyuanjin.imseven.ui.fragments.thirdfragment.dialog.MyDialog;
import com.quyuanjin.imseven.ui.view.BottomBar;
import com.quyuanjin.imseven.ui.view.wedgit.adapter.HomeRVadapter;
import com.quyuanjin.imseven.ui.view.wedgit.pullextend.ExtendLayout;
import com.quyuanjin.imseven.ui.view.wedgit.pullextend.ExtendListFooter;
import com.quyuanjin.imseven.ui.view.wedgit.pullextend.ExtendListHeader;
import com.quyuanjin.imseven.ui.view.wedgit.pullextend.IExtendLayout;
import com.quyuanjin.imseven.ui.view.wedgit.pullextend.PullExtendLayout;
import com.quyuanjin.imseven.utils.DetailTransition;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.ToastUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import highperformancefriendscircle.FriendCircleActivity;
import me.yokeyword.fragmentation.SupportFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentMine extends BaseMainFragment {
    public static SupportFragment newInstance() {
        Bundle args = new Bundle();

        FragmentMine fragment = new FragmentMine();
        fragment.setArguments(args);
        return fragment;
    }

    private View view;
    String name;
    String des;
    String sex;
    String por;
    String userid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

   private TextView name1;
    private TextView sign;
    private void initView(View view) {
         name1 = view.findViewById(R.id.tv_name);
          sign = view.findViewById(R.id.tv_fans);
        final PullExtendLayout pullExtendLayout = view.findViewById(R.id.pull_extend);
        //   pullExtendLayout.scrollTo(1000,1000);
        ExtendListHeader extendListHeader = view.findViewById(R.id.extend_header);
        // extendListHeader.onSearchClicked(this, FragmentSearchWorld.newInstance());
        extendListHeader.setStateLayout(new ExtendLayout.StateLayout() {

            @Override
            public void onStateChange(IExtendLayout.State state) {
                BottomBar bottomBar = getParentFragment().getView().findViewById(R.id.bottomBar);
                ImageView bottomImg = getParentFragment().getView().findViewById(R.id.bottomImg);

                switch (state) {
                    case RESET:
                        bottomBar.setVisibility(View.VISIBLE);
                        bottomImg.setVisibility(View.GONE);
                        break;


                    case arrivedListHeight:
                        bottomBar.setVisibility(View.GONE);
                        bottomImg.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }
        });

        TextView search = view.findViewById(R.id.search_world);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainFragment) getParentFragment()).startBrotherFragment(FragmentSearchWorld.newInstance());

            }
        });


//        ExtendListFooter extendFooter = view.findViewById(R.id.extend_footer);
//        RecyclerView extendFooterRV = extendFooter.getRecyclerView();
//        extendFooterRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        List<String> footList = Arrays.asList("脚程序1", "脚程序2", "脚程序3", "脚程序4", "脚程序5", "脚程序6");
//        HomeRVadapter footRVadapter = new HomeRVadapter(R.layout.home_program_item, footList);
//        extendFooterRV.setAdapter(footRVadapter);

    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        name = (String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "name", "");
        des = (String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "des", "");

        name1.setText(name);

        sign.setText(des);

    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();


    }

    @Override
    public void onResume() {
        super.onResume();


    }



    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        initListener();
        sex = (String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "sex", "");
        por = (String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "portrait", "");
        userid = (String) SharedPreferencesUtils.getParam(getContext().getApplicationContext(), "userid", "");

        ImageView imageView = view.findViewById(R.id.img_avatar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ((MainFragment) getParentFragment()).startBrotherFragment(FragmentMineDetail.newInstance());

            }
        });
    }


    private void initListener() {
        LinearLayout myFriendCircle = view.findViewById(R.id.lay_sign);
        myFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(_mActivity, FriendCircleActivity.class));

            }
        });

        LinearLayout myScanCode = view.findViewById(R.id.lay_agent);
        myScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOrDis();
            }
        });

        LinearLayout lay_addfriend = view.findViewById(R.id.lay_new_friend);
        lay_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainFragment) getParentFragment()).startBrotherFragment(FragmentNewFriend.newInstance());

            }
        });

        LinearLayout linearLayout_momo = view.findViewById(R.id.lay_luck);
        linearLayout_momo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainFragment) getParentFragment()).startBrotherFragment(FragmentMomo.newInstance());
            }
        });

        LinearLayout linearLayout_set = view.findViewById(R.id.lay_set);
        linearLayout_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainFragment) getParentFragment()).startBrotherFragment(FragmentSetting.newInstance());
            }
        });
    }


    private void showDialogOrDis() {
        MyDialog myAdvertisementView = new MyDialog(_mActivity);
        myAdvertisementView.showDialog();
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
    public void UnReadAddFriend2(UnReadAddFriendRequest unReadAddFriendRequest) {
        // Log.d("looaa","fragmentmine收到了");

        ToastUtils.show(_mActivity, unReadAddFriendRequest.getUserid());
    }
}
