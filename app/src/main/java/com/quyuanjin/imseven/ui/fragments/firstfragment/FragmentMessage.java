package com.quyuanjin.imseven.ui.fragments.firstfragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quyuanjin.imseven.App;
import com.quyuanjin.imseven.MainFragment;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.adapter.RecentMsgRecycleAdapter;
import com.quyuanjin.imseven.base.BaseMainFragment;
import com.quyuanjin.imseven.eventbus.ChatTellRecementMsg;
import com.quyuanjin.imseven.eventbus.MsgBus;
import com.quyuanjin.imseven.eventbus.MsgBusTo;
import com.quyuanjin.imseven.pojo.ChatParce;
import com.quyuanjin.imseven.pojo.Msg;
import com.quyuanjin.imseven.pojo.PojoContract;
import com.quyuanjin.imseven.pojo.PojoContractDao;
import com.quyuanjin.imseven.pojo.PojoRecementMsg;

import com.quyuanjin.imseven.pojo.PojoRecementMsgDao;
import com.quyuanjin.imseven.ui.activity.login.User;
import com.quyuanjin.imseven.ui.fragments.addfragment.FragmentAdd;
import com.quyuanjin.imseven.ui.fragments.addfriendsfragment.FragmentAddFriend;
import com.quyuanjin.imseven.ui.fragments.chatfragment.FragmentChat;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.TimeHelper;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemLongClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;

import static com.quyuanjin.imseven.netty.ChatClientHandler.changeMsg;

public class FragmentMessage extends BaseMainFragment {
    public static SupportFragment newInstance() {
        Bundle args = new Bundle();

        FragmentMessage fragment = new FragmentMessage();
        fragment.setArguments(args);
        return fragment;
    }

    private LinearLayout toolbar;
    private NestedScrollView scrollView;
    private View view;
    public SwipeRecyclerView mmSwipeRecyclerView;//??????RecyclerView
    private ArrayList<PojoRecementMsg> entityList = new ArrayList<>();
    private RecentMsgRecycleAdapter mRecentMsgRecycleAdapter;
    private TopRightMenu mTopRightMenu;
    private static final int REQUEST_CODE_SCAN = 9;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);


        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView(view);
        initRecyclerView();
        initData();
        initAddButtom();
        getUnReadMsgFromNet();
    }

    private void initAddButtom() {
        final ImageView imageView = view.findViewById(R.id.selected_add);
        final ImageView imageView2 = view.findViewById(R.id.selected_add2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTopRightMenu = new TopRightMenu(_mActivity);

                //???????????????
                List<MenuItem> menuItems = new ArrayList<>();
                menuItems.add(new MenuItem(R.mipmap.ic_around_blue, "?????????"));
                menuItems.add(new MenuItem(R.mipmap.ic_scan_blue, "?????????"));

                mTopRightMenu
                        .setHeight(480)     //????????????480
                        .setWidth(320)      //????????????wrap_content
                        .showIcon(true)     //??????????????????????????????true
                        .dimBackground(true)        //????????????????????????true
                        .needAnimationStyle(true)   //????????????????????????true
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                        .addMenuList(menuItems)
                        .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                if (position == 0) {

                                    ((MainFragment) getParentFragment()).startBrotherFragment(FragmentAdd.newInstance());

                                } else if (position == 1) {
                                    AndPermission.with(FragmentMessage.this)
                                            .runtime()
                                            .permission(Permission.CAMERA)
                                            .onGranted(new Action<List<String>>() {
                                                @Override
                                                public void onAction(List<String> data) {

                                                    Intent intent = new Intent(_mActivity, CaptureActivity.class);
                                                    /*ZxingConfig????????????
                                                     *????????????????????????????????????????????????????????????
                                                     * ?????????????????????  ??????
                                                     * ????????????????????????
                                                     * ???????????????????????????
                                                     * */
                                                    ZxingConfig config = new ZxingConfig();
                                                    config.setPlayBeep(true);//???????????????????????? ?????????true
                                                    config.setShake(true);//????????????  ?????????true
                                                    config.setDecodeBarCode(true);//????????????????????? ?????????true
                                                    config.setReactColor(R.color.colorAccent);//????????????????????????????????? ???????????????
                                                    config.setFrameLineColor(R.color.colorAccent);//??????????????????????????? ????????????
                                                    config.setScanLineColor(R.color.colorAccent);//???????????????????????? ????????????
                                                    config.setFullScreenScan(true);//??????????????????  ?????????true  ??????false??????????????????????????????
                                                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                                                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                                                }
                                            }).onDenied(new Action<List<String>>() {
                                        @Override
                                        public void onAction(List<String> data) {

                                        }
                                    })
                                            .start();


                                }


                            }
                        })
                        .showAsDropDown(imageView, -225, 100);    //????????????
//      		.showAsDropDown(moreBtn)
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTopRightMenu = new TopRightMenu(_mActivity);

                //???????????????
                List<MenuItem> menuItems = new ArrayList<>();
                menuItems.add(new MenuItem(R.mipmap.ic_around_blue, "?????????"));
                menuItems.add(new MenuItem(R.mipmap.ic_scan_blue, "?????????"));

                mTopRightMenu
                        .setHeight(480)     //????????????480
                        .setWidth(320)      //????????????wrap_content
                        .showIcon(true)     //??????????????????????????????true
                        .dimBackground(true)        //????????????????????????true
                        .needAnimationStyle(true)   //????????????????????????true
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                        .addMenuList(menuItems)
                        .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                if (position == 0) {
                                    ((MainFragment) getParentFragment()).startBrotherFragment(FragmentAdd.newInstance());

                                } else if (position == 1) {
                                    AndPermission.with(FragmentMessage.this)
                                            .runtime()
                                            .permission(Permission.CAMERA)
                                            .onGranted(new Action<List<String>>() {
                                                @Override
                                                public void onAction(List<String> data) {
                                                    Intent intent = new Intent(_mActivity, CaptureActivity.class);
                                                    /*ZxingConfig????????????
                                                     *????????????????????????????????????????????????????????????
                                                     * ?????????????????????  ??????
                                                     * ????????????????????????
                                                     * ???????????????????????????
                                                     * */
                                                    ZxingConfig config = new ZxingConfig();
                                                    config.setPlayBeep(true);//???????????????????????? ?????????true
                                                    config.setShake(true);//????????????  ?????????true
                                                    config.setDecodeBarCode(true);//????????????????????? ?????????true

                                                    config.setScanLineColor(R.color.colorPrimary);//???????????????????????? ????????????
                                                    config.setFullScreenScan(true);//??????????????????  ?????????true  ??????false??????????????????????????????
                                                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                                                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                                                }
                                            }).onDenied(new Action<List<String>>() {
                                        @Override
                                        public void onAction(List<String> data) {

                                        }
                                    })
                                            .start();


                                }


                            }
                        })
                        .showAsDropDown(imageView2, -225, 100);    //????????????
//      		.showAsDropDown(moreBtn)
            }
        });
    }

    private String userid;

    private void initData() {
        userid = (String) SharedPreferencesUtils.getParam(_mActivity, "userid", "");

        entityList.add(new PojoRecementMsg(123L, "666", "666", "666",
                "666", "666", "666", "",
                "666", "666", "666"));
        List<PojoRecementMsg> pojoRecementMsgList=App.getDaoSession().getPojoRecementMsgDao().queryBuilder().list();
        Collections.reverse(pojoRecementMsgList);

        entityList.addAll(pojoRecementMsgList);

        mRecentMsgRecycleAdapter.notifyDataSetChanged();
    }


    private void initRecyclerView() {
        mmSwipeRecyclerView = view.findViewById(R.id.rvRecentMessage);
        mRecentMsgRecycleAdapter = new RecentMsgRecycleAdapter(getContext(), entityList);
        mmSwipeRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mmSwipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mmSwipeRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);
        mmSwipeRecyclerView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int adapterPosition) {

            }
        });
        mmSwipeRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition) {
                //????????????fragment
                PojoRecementMsg pojoRecementMsg = mRecentMsgRecycleAdapter.getMsg(adapterPosition);

                ChatParce parce = new ChatParce(pojoRecementMsg.getId(),
                        pojoRecementMsg.getMyUserId(), pojoRecementMsg.getYourUserId(),
                        "??????????????????", pojoRecementMsg.getPortrait());
                ((MainFragment) getParentFragment()).startBrotherFragment(FragmentChat.newInstance(parce));


            }
        });

        //  mmSwipeRecyclerView.setNestedScrollingEnabled(false);

        mmSwipeRecyclerView.setAdapter(mRecentMsgRecycleAdapter);
        mRecentMsgRecycleAdapter.notifyDataSetChanged();

    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        scrollView = view.findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {//????????????
                    //  toolbar.setVisibility(View.VISIBLE);
                    //  mmSwipeRecyclerView.setNestedScrollingEnabled(false);
                }
                if (scrollY < oldScrollY) {//????????????
                    //   toolbar.setVisibility(View.GONE);
                    //    mmSwipeRecyclerView.setNestedScrollingEnabled(false);

                }
                if (scrollY == 0) {// ????????????
                    //   mmSwipeRecyclerView.setNestedScrollingEnabled(true);
                }
                // ????????????
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                }
                if (scrollY > 100) {
                    toolbar.setVisibility(View.VISIBLE);
                    if (scrollY < 355) {
                        toolbar.getBackground().setAlpha(scrollY - 100);
                    }

                } else {
                    toolbar.setVisibility(View.GONE);

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ???????????????/????????????
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                //    content???user ???uuid

                ChatParce parce = new ChatParce(null,
                        userid, content, "00", null);

                ((MainFragment) getParentFragment()).startBrotherFragment(FragmentAddFriend.newInstance(parce));

                Toast.makeText(_mActivity, content, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * ?????????????????????Item?????????????????????????????????
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);

            // 1. MATCH_PARENT ???????????????????????????Item?????????;
            // 2. ???????????????????????????80;
            // 3. WRAP_CONTENT???????????????????????????;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // ??????????????????????????????????????????????????????????????????
        /*    {


                SwipeMenuItem closeItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.selector_green)
                        .setImage(R.drawable.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(closeItem); // ????????????????????????
            }*/

            // ??????????????????????????????????????????????????????????????????
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.selector_green)
                        .setImage(R.drawable.ic_action_close)
                        .setText("??????")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// ????????????????????????

                SwipeMenuItem addItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.selector_green)
                        .setText("??????")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // ????????????????????????
            }
        }
    };
    /**
     * RecyclerView???Item???Menu???????????????
     */
    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // ???????????????????????????
            int menuPosition = menuBridge.getPosition(); // ?????????RecyclerView???Item??????Position???

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {

                Toast.makeText(getContext(), "list???" + position + "; ???????????????" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
                //todo ???????????????????????????????????????????????? ???????????????????????????
                PojoRecementMsg pojoRecementMsg = App.getDaoSession().getPojoRecementMsgDao().queryBuilder().
                        where(PojoRecementMsgDao.Properties.YourUserId.eq(entityList.get(position).getYourUserId())).unique();
                App.getDaoSession().getPojoRecementMsgDao().delete(pojoRecementMsg);

                entityList.remove(position);
                mRecentMsgRecycleAdapter.notifyDataSetChanged();


            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(getContext(), "list???" + position + "; ???????????????" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

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
    public void UnReadAddFriendFragemntMsg(PojoRecementMsg pojoRecementMsg) {
        // Log.d("looaa","fragmentmine?????????");
        //ToastUtils.show(_mActivity,"fragmentmine?????????eventbus");

        entityList.add(pojoRecementMsg);


        mRecentMsgRecycleAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void ChatTellMSg(ChatTellRecementMsg chatTellRecementMsg) {

        MsgToPojoContract(chatTellRecementMsg.getMsg());


    }

    private void MsgToPojoContract(Msg msg) {

        PojoRecementMsg pojoRecementMsg = App.getDaoSession().getPojoRecementMsgDao().queryBuilder().
                where(PojoRecementMsgDao.Properties.YourUserId.eq(msg.getSendID())).unique();
        if (pojoRecementMsg != null) {//??????????????????????????????????????????????????????????????????????????????????????????
            //   ToastUtils.show(_mActivity, "?????????????????????");
            for (int i = 0; i < entityList.size(); i++) {
                if (entityList.get(i).getYourUserId().equals(pojoRecementMsg.getYourUserId())) {
                    //????????????
                    entityList.get(i).setContentText(msg.getMsg());

                    Collections.swap(entityList, 0, i);
                    mRecentMsgRecycleAdapter.notifyDataSetChanged();

                    pojoRecementMsg.setContentText(msg.getMsg());
                    App.getDaoSession().getPojoRecementMsgDao().update(pojoRecementMsg);

                }
            }


        } else {//???????????????
            //     ToastUtils.show(_mActivity, "?????????????????????????????????????????????????????????????????????" );
            //Log.d("looaa","?????????????????????????????????????????????????????????????????????");
            List<PojoContract> pojoContractList = App.getDaoSession().getPojoContractDao().loadAll();

//            Log.d("669",String.valueOf(pojoContractList.size()));
//            Log.d("669",String.valueOf(pojoContractList.get(0).getUserid()));
//            Log.d("669",msg.getReceiveId()  );
//            Log.d("669",msg.getSendID()  );

            PojoContract pojoContract = App.getDaoSession().getPojoContractDao()
                    .queryBuilder()
                    .where(PojoContractDao.Properties.Userid.eq(msg.getSendID())).unique();


            PojoRecementMsg pojoRecementMsg1 = new PojoRecementMsg(null,
                    pojoContract.getMyUserId(), pojoContract.getUserid(),
                    pojoContract.getPortraitImageView(), "", pojoContract.getNameTextView(),
                    TimeHelper.getCurrentTime(), "", "", msg.getMsg(), "");


//            PojoRecementMsg pojoRecementMsg1 = new PojoRecementMsg(null,
//                    msg.getSendID() ,msg.getReceiveId() ,
//                    msg.getNetPath(), "", ,
//                    TimeHelper.getCurrentTime(), "", "", msg.getMsg(), "");

            entityList.add(pojoRecementMsg1);

            mRecentMsgRecycleAdapter.notifyDataSetChanged();

            App.getDaoSession().getPojoRecementMsgDao().insert(pojoRecementMsg1);
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void getMsgFromEvent(MsgBusTo msgbus) {
        Msg msg = msgbus.getMsg();
//        ToastUtils.show(_mActivity, "???????????????" + msg.getSendID() + "?????????????????????" +
//                msg.getMsg());
        MsgToPojoContract(msg);
    }

    void getUnReadMsgFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url(com.quyuanjin.imseven.Constant.URL_save + "getUnReadMsg")
                        .addParams("userid", userid)

                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.equals("01")) {
                            ToastUtils.show(_mActivity, "???????????????");
                        } else {
                            Gson gson1 = new Gson();
                            List<Msg> list = gson1.fromJson(response, new TypeToken<List<Msg>>() {
                            }.getType());
                            ToastUtils.show(_mActivity, "???????????????");
                            for (Msg msg : list) {
                                EventBus.getDefault().post(new MsgBusTo(msg));// ??????????????????
                                //????????????????????????????????????????????????????????????chat
                                EventBus.getDefault().post(new MsgBus(changeMsg(msg)));//????????????????????????

                            }

                        }
                    }
                });
            }
        }).start();
    }
}
