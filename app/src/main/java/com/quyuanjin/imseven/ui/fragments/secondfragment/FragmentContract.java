package com.quyuanjin.imseven.ui.fragments.secondfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allenliu.sidebar.ISideBarSelectCallBack;
import com.allenliu.sidebar.SideBar;
import com.quyuanjin.imseven.App;
import com.quyuanjin.imseven.Constant;
import com.quyuanjin.imseven.MainFragment;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.adapter.ContractAdapter;
import com.quyuanjin.imseven.base.BaseMainFragment;
import com.quyuanjin.imseven.pojo.ChatParce;
import com.quyuanjin.imseven.pojo.PojoContract;
import com.quyuanjin.imseven.ui.fragments.detailfragment.FragmentDetail;
import com.quyuanjin.imseven.utils.PinYinSortUtils;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemLongClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import me.yokeyword.fragmentation.SupportFragment;

public class FragmentContract extends BaseMainFragment {
    public static SupportFragment newInstance() {
        Bundle args = new Bundle();
        FragmentContract fragment = new FragmentContract();
        fragment.setArguments(args);
        return fragment;
    }

    private View view;
    private SwipeRecyclerView contractRecyclerView;
    private ContractAdapter contractAdapter;
    private ArrayList<PojoContract> entityList = new ArrayList<>();
    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    private HashMap<String, Integer> locMap = new HashMap<>();
    private SideBar bar;

    ArrayList<PojoContract> mEntityList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmnet_contract, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();

        initRecyclerView();
        initSideBar();
    }

    private void initData() {
        mEntityList = new ArrayList<>();
        mEntityList.add(new PojoContract(25L, "", "王五", "王五", "", "", Constant.TAG_ITEM, "", "1234", ""));
        mEntityList.add(new PojoContract(25L, "", "赵五", "赵五", "", "", Constant.TAG_ITEM, "", "1234", ""));

        mEntityList.add(new PojoContract(25L, "", "李五", "李五", "", "", Constant.TAG_ITEM, "", "3214", ""));

        mEntityList.add(new PojoContract(25L, "", "张五", "张五", "", "", Constant.TAG_ITEM, "", "12412", ""));
        mEntityList.add(new PojoContract(25L, "", "钱五", "钱五", "", "", Constant.TAG_ITEM, "", "124124", ""));

        mEntityList.add(new PojoContract(25L, "", "孙五", "孙五", "", "", Constant.TAG_ITEM, "", "12312", ""));
        mEntityList.add(new PojoContract(25L, "", "周五", "周五", "", "", Constant.TAG_ITEM, "", "4124", ""));
        mEntityList.add(new PojoContract(25L, "", "冯五", "冯五", "", "", Constant.TAG_ITEM, "", "21412", ""));
        mEntityList.addAll(App.getDaoSession().getPojoContractDao().loadAll());

        initDataAndSort(mEntityList);
    }

    private void initSideBar() {
        bar = (SideBar) view.findViewById(R.id.bar);
        bar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                //    Toast.makeText(getContext(),String.valueOf(index),Toast.LENGTH_SHORT).show();
                //   Log.d("tag1", String.valueOf(index) + "" + selectStr);
                Toast.makeText(getContext(), String.valueOf(index), Toast.LENGTH_SHORT).show();

                if (locMap.containsKey(selectStr)) {

                    if (locMap.get(selectStr) != null) {
                        int pos = locMap.get(selectStr);
                        smoothMoveToPosition(contractRecyclerView, pos);


                    }

                }

            }
        });
    }

    private void initRecyclerView() {
        contractRecyclerView = view.findViewById(R.id.contract_recycler);
        //  contractRecyclerView.setNestedScrollingEnabled(false);
        contractAdapter = new ContractAdapter(entityList, getContext());
        contractRecyclerView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(getContext(), R.color.main_bg1)));
        contractRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(contractRecyclerView, mToPosition);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //  contractRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        contractRecyclerView.setLayoutManager(layoutManager);
        contractRecyclerView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int adapterPosition) {
            }
        });

        contractRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition) {
                //     ToastUtils.show(getContext(), "dianjile" + adapterPosition+entityList.get(adapterPosition).getTag());
                if (!(Constant.TAG_STICKY.equals(entityList.get(adapterPosition).getTag()))) {
                    PojoContract pojoContract = entityList.get(adapterPosition);
                    ChatParce chatParce = new ChatParce(pojoContract.getId(),
                            pojoContract.getMyUserId(), pojoContract.getUserid(),
                            "本地头像地址", pojoContract.getPortraitImageView());

                    ((MainFragment) getParentFragment()).startBrotherFragment(FragmentDetail.newInstance(chatParce));


                }
            }
        });

        contractRecyclerView.setAdapter(contractAdapter);
        contractAdapter.notifyDataSetChanged();
    }

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    //
    private void initDataAndSort(ArrayList<PojoContract> list2) {

        //List<PojoContract> pojoContracts = App.getDaoSession().getPojoContractDao().loadAll();
        //entityList.addAll(pojoContracts);
        //  entityList.addAll(list2);

        PinYinSortUtils pinYinSortUtil = new PinYinSortUtils(list2);
        entityList.addAll(pinYinSortUtil.sortAndAdd());

        locMap = pinYinSortUtil.getPosList();

        // contractAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
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
    public void UnReadAddFriendFragmentContract(PojoContract pojoContract) {
        // Log.d("looaa","fragmentmine收到了");
        ToastUtils.show(_mActivity, "fragmentContract响应了eventbus");
       if (mEntityList.size()>0){
           mEntityList.clear();
       }

        initData();
        contractAdapter.notifyDataSetChanged();

    }

}
