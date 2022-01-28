package com.quyuanjin.imseven.ui.fragments.chatfragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.language.PictureLanguageUtils;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.obs.services.IFSClient;
import com.obs.services.ObsClient;
import com.obs.services.model.BucketStorageInfo;
import com.quyuanjin.imseven.App;
import com.quyuanjin.imseven.Constant;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.adapter.ChatAdapter;
import com.quyuanjin.imseven.base.BaseBackFragment;
import com.quyuanjin.imseven.eventbus.BitMapUrl;
import com.quyuanjin.imseven.eventbus.ChatTellRecementMsg;
import com.quyuanjin.imseven.eventbus.MsgBus;
import com.quyuanjin.imseven.eventbus.UrlMsg;
import com.quyuanjin.imseven.pojo.AllTypeMsg;
import com.quyuanjin.imseven.pojo.ChatParce;
import com.quyuanjin.imseven.pojo.Msg;
import com.quyuanjin.imseven.pojo.MsgDao;
import com.quyuanjin.imseven.pojo.PojoRecementMsg;
import com.quyuanjin.imseven.ui.activity.GaoDeMap;
import com.quyuanjin.imseven.ui.fragments.chatfragment.friendsettingfragment.FragmentFriendDetailSetting;
import com.quyuanjin.imseven.ui.fragments.chatfragment.wechattakevideo.FragmentTakeVidoe;
import com.quyuanjin.imseven.ui.fragments.firstfragment.userdetailfragment.FragmentUserDetail;
import com.quyuanjin.imseven.ui.weixin_recorder.MediaManager;
import com.quyuanjin.imseven.ui.weixin_recorder.view.AudioRecorderButton;
import com.quyuanjin.imseven.update.UpDate;
import com.quyuanjin.imseven.utils.KeyboardUtil;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.SoftKeyboardUtils;
import com.quyuanjin.imseven.utils.TimeHelper;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.quyuanjin.imseven.utils.pictureselect.GlideEngine;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.impl.ScrollBoundaryDeciderAdapter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vanniktech.emoji.EmojiPopup;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.Id;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import livestreaming.GetLiveStream;
import livestreaming.LiveMain;
import livestreaming.PushMain;
import okhttp3.Call;
import okhttp3.OkHttpClient;


public class FragmentChat extends BaseBackFragment implements View.OnClickListener {

    private View view;
    private CommonTitleBar titlebar;

    private EditText mEditText;
    private Button sendBtn;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private LinearLayout llrootView;
    private AudioRecorderButton id_recorder_button;

    //加号点出来的
    private ImageView ivAlbum;
    private ImageView ivShot;
    private ImageView ivLocation;
    private ImageView ivVideo;
    private ImageView ivMore;
    private LinearLayout bolck_titlebar;
    private EmojiPopup emojiPopup;
    private ImageView ivAudio;
    private List<LocalMedia> selectList1 = new ArrayList<>();
    ArrayList<Msg> mList = new ArrayList<>();
    ArrayList<Integer> imglist = new ArrayList<>();

    ChatAdapter adapter;
    private ImageView ivAudiosend;
    private ImageView ivAudiorece;

    private static final String ARG_MSG = "arg_msg";
    private ChatParce chatParce;
    private String userid;
    private LinearLayout buttomPanel;

    public static FragmentChat newInstance(ChatParce msg) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MSG, msg);
        FragmentChat fragment = new FragmentChat();
        fragment.setArguments(args);
        return fragment;
    }

    private ObsClient obsClient;
    String upOrDownPoint = "https://t011t.obs.cn-east-2.myhuaweicloud.com/";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatParce = getArguments().getParcelable(ARG_MSG);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        String endPoint = "https://obs.cn-east-2.myhuaweicloud.com";
        String ak = "E8WBUTSHZETZTLHTLSIM";
        String sk = "WN0CuRakbsNJfWXXHp1l2ZaeMhD5EAfNi1FdHTBE";
// 创建ObsClient实例
        obsClient = new ObsClient(ak, sk, endPoint);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(view);
        userid = (String) SharedPreferencesUtils.getParam(_mActivity, "userid", "");
        PermissionGen.with(_mActivity)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA ,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.LOCATION_HARDWARE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .request();
//        PermissionGen.with(FragmentChat.this)
//                .addRequestCode(100)
//                .permissions(
//                        Manifest.permission.RECORD_AUDIO
//                        , Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return attachToSwipeBack(view);
    }

    private void initView(View view) {
        buttomPanel = view.findViewById(R.id.bottom_panel);
        mEditText = view.findViewById(R.id.etContent);
        sendBtn = view.findViewById(R.id.btnSend);
        recyclerView = view.findViewById(R.id.rvMsg);
        imageView = view.findViewById(R.id.ivEmo);
        llrootView = view.findViewById(R.id.llRoot);
        ivAudio = view.findViewById(R.id.ivAudio);

        titlebar = view.findViewById(R.id.titlebar);
        id_recorder_button = view.findViewById(R.id.id_recorder_button);
        ivMore = view.findViewById(R.id.ivMore);
        bolck_titlebar = view.findViewById(R.id.bolck_titlebar);
        ivAlbum = view.findViewById(R.id.ivAlbum);
        ivShot = view.findViewById(R.id.ivShot);
        ivLocation = view.findViewById(R.id.ivLocation);
        ivVideo = view.findViewById(R.id.ivVideo);
        emojiPopup = EmojiPopup.Builder.fromRootView(view).build(mEditText);


        /**
         * 拉流测试段
         */
        ImageView imageView2 = view.findViewById(R.id.iv_test);
        imageView2.setOnClickListener(this);


        bolck_titlebar.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        ivAlbum.setOnClickListener(this);
        ivShot.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        ivVideo.setOnClickListener(this);
        imageView.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        ivAudio.setOnClickListener(this);

        titlebar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    _mActivity.onBackPressed();
                } else if (action == CommonTitleBar.ACTION_RIGHT_TEXT) {
                    start(FragmentFriendDetailSetting.newInstance());
                }
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        new KeyboardUtil(this, view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        // layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        mList.add(new Msg(null, "", "", "你好", "", "", "", "", 0, 0, "", "", ""));
        initDataFromLocal();

        adapter = new ChatAdapter(_mActivity, mList, selectList1, imglist);
        adapter.setHasStableIds(true);
        adapter.setOnItemLongClickListener(new ChatAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(_mActivity, "这是条目" + position, Toast.LENGTH_LONG).show();

                PopupMenu popupMenu = new PopupMenu(_mActivity, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());

                //弹出式菜单的菜单项点击事件
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.fanyi:
                                adapter.getMsg(position).setFanyi(adapter.getMsg(position)
                                        .getMsg());
                                adapter.notifyDataSetChanged();
                                break;
                            case R.id.chehui:
                                ToastUtils.show(_mActivity, "1");

                                break;
                            case R.id.removeItem:
                                mList.remove(position);
                                adapter.notifyItemRemoved(position);
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }


        });
        adapter.setOnItemImageHeadClickListener(new ChatAdapter.OnItemImageHeadClickListener() {
            @Override
            public void onItemImageHeadClick(View view, int position) {
                start(FragmentUserDetail.newInstance());
            }
        });
        adapter.setOnItemVoiceClickListener(new ChatAdapter.OnItemVoiceClickListener() {
            @Override
            public void onItemVoiceClick(View view, int position) {
                if (Constant.item_audio_send == mList.get(position).getContentType()) {


                    //如果ling一个动画正在运行， 停止第一个播放其他的
                    if (ivAudiosend != null) {
                        ivAudiosend.setBackgroundResource(R.drawable.adj);

                        ivAudiosend = null;
                    }
                    //播放动画
                    ivAudiosend = view.findViewById(R.id.ivAudiosend);
                    ivAudiosend.setBackgroundResource(R.drawable.audio_animation_right_list);
                    final AnimationDrawable animation = (AnimationDrawable) ivAudiosend.getBackground();
                    animation.start();

                    //播放音频  完成后改回原来的background
                    MediaManager.playSound(mList.get(position).getLocalPath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            ivAudiosend.setBackgroundResource(R.drawable.adj);
                        }
                    });
                } else {


                    //如果ling一个动画正在运行， 停止第一个播放其他的
                    if (ivAudiorece != null) {
                        ivAudiorece.setBackgroundResource(R.drawable.adj);

                        ivAudiorece = null;
                    }
                    //播放动画
                    ivAudiorece = view.findViewById(R.id.ivAudiorece);
                    ivAudiorece.setBackgroundResource(R.drawable.audio_animation_right_list);
                    final AnimationDrawable animation2 = (AnimationDrawable) ivAudiorece.getBackground();
                    animation2.start();

                    //播放音频  完成后改回原来的background
                    MediaManager.playSound(mList.get(position).getNetPath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            ivAudiorece.setBackgroundResource(R.drawable.adj);
                        }
                    });
                }

            }
        });

        adapter.setOnItemVoiceClickListener1(new ChatAdapter.OnItemVoiceClickListener1() {
            @Override
            public void onItemVoiceClick1(View view, int position) {
                if (Constant.item_audio_send == mList.get(position).getContentType()) {


                    //如果ling一个动画正在运行， 停止第一个播放其他的
                    if (ivAudiosend != null) {
                        ivAudiosend.setBackgroundResource(R.drawable.adj);

                        ivAudiosend = null;
                    }
                    //播放动画
                    ivAudiosend = view.findViewById(R.id.ivAudiosend);
                    ivAudiosend.setBackgroundResource(R.drawable.audio_animation_right_list);
                    final AnimationDrawable animation = (AnimationDrawable) ivAudiosend.getBackground();
                    animation.start();

                    //播放音频  完成后改回原来的background
                    MediaManager.playSound(mList.get(position).getNetPath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            ivAudiosend.setBackgroundResource(R.drawable.adj);
                        }
                    });
                } else {


                    //如果ling一个动画正在运行， 停止第一个播放其他的
                    if (ivAudiorece != null) {
                        ivAudiorece.setBackgroundResource(R.drawable.adj);

                        ivAudiorece = null;
                    }
                    //播放动画
                    ivAudiorece = view.findViewById(R.id.ivAudiorece);
                    ivAudiorece.setBackgroundResource(R.drawable.audio_animation_right_list);
                    final AnimationDrawable animation2 = (AnimationDrawable) ivAudiorece.getBackground();
                    animation2.start();

                    //播放音频  完成后改回原来的background
                    MediaManager.playSound(mList.get(position).getNetPath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            ivAudiorece.setBackgroundResource(R.drawable.adj);
                        }
                    });
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setScaleY(-1);//必须设置
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (SoftKeyboardUtils.isSoftShowing(_mActivity)) {
                    SoftKeyboardUtils.hideSystemSoftKeyboard(_mActivity);
                }
                if (bolck_titlebar.getVisibility() == View.VISIBLE) {
                    bolck_titlebar.setVisibility(View.GONE);
                }

                if (emojiPopup.isShowing()) {
                    emojiPopup.dismiss();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });
        recyclerView.scrollToPosition(mList.size() - 1);
        mEditText.setOnClickListener(this);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()) {
                    emojiPopup.dismiss();
                }
                if (bolck_titlebar.getVisibility() == View.VISIBLE) {
                    bolck_titlebar.setVisibility(View.GONE);
                }

                recyclerView.scrollToPosition(mList.size() - 1);
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    sendBtn.setVisibility(View.VISIBLE);
                    ivMore.setVisibility(View.GONE);
                } else {
                    sendBtn.setVisibility(View.GONE);
                    ivMore.setVisibility(View.VISIBLE);
                }

            }
        });

        audioListener();

        initRefresh();
        // startPictureSelector2(PictureConfig.CHOOSE_REQUEST);//选背景图
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        // 入场动画结束后执行  优化,防动画卡顿
        UpDate upDate=new UpDate( _mActivity);
        upDate.checkVersion();
    }

    private void initDataFromLocal() {
        List<Msg> msgList = App.getDaoSession()
                .getMsgDao().queryBuilder()
                .whereOr(MsgDao.Properties.SendID.eq(userid),
                        MsgDao.Properties.SendID.eq(chatParce.yourUserId))
                .list();
        for (int i = 0; i < msgList.size(); i++) {
            Msg msg = msgList.get(i);
            mList.add(msg);
            if (Constant.item_image_receive == msg.getContentType()
            ) {
                imglist.add(mList.size() - 1);
                selectList1.add(new LocalMedia(msg.getNetPath(), 0,
                        PictureSelectionConfig.getInstance().chooseMode, PictureMimeType.MIME_TYPE_IMAGE));
            } else if (Constant.item_video_receive == msg.getContentType()
            ) {
                imglist.add(mList.size() - 1);
                selectList1.add(new LocalMedia(msg.getNetPath(), 10,
                        PictureSelectionConfig.getInstance().chooseMode, PictureMimeType.MIME_TYPE_VIDEO));
            } else if (Constant.item_image_send == msg.getContentType()) {
                imglist.add(mList.size() - 1);
                selectList1.add(new LocalMedia(msg.getLocalPath(), 0,
                        PictureSelectionConfig.getInstance().chooseMode, PictureMimeType.MIME_TYPE_IMAGE));

            } else if (Constant.item_video_send == msg.getContentType()) {
                imglist.add(mList.size() - 1);
                selectList1.add(new LocalMedia(msg.getLocalPath(), 10,
                        PictureSelectionConfig.getInstance().chooseMode, PictureMimeType.MIME_TYPE_VIDEO));

            }
        }

    }

    private void initRefresh() {
        final RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);//必须关闭
        refreshLayout.setEnableAutoLoadMore(true);//必须关闭
        refreshLayout.setEnableNestedScroll(false);//必须关闭
        refreshLayout.setEnableScrollContentWhenLoaded(true);//必须关闭
        refreshLayout.getLayout().setScaleY(-1);//必须设置

        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter() {
            @Override
            public boolean canLoadMore(View content) {
                return super.canRefresh(content);//必须替换
            }
        });

        //监听加载，而不是监听 刷新
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        mList.add(0, new Msg(null, "",
                                "", UUID.randomUUID().toString(), "", "", "",
                                "", 0, 0,
                                "", "", ""));
                        if (imglist.size() > 0) {
                            //每刷新n条，imglist 消除n位
                            imglist.remove(0);
                            imglist.add(mList.size() - 1);
                        }
                        adapter.notifyItemInserted(0);
                        adapter.notifyItemRangeChanged(0, mList.size());
                        refreshLayout.finishLoadMore();

                    }
                }, 1000);
            }
        });

    }

    private void audioListener() {

        id_recorder_button.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                //每完成一次录音
                //   Recorder recorder = new Recorder(seconds, filePath);
                Msg msg = new Msg(null, userid, chatParce.yourUserId, "", UUID.randomUUID().toString(),
                        TimeHelper.getCurrentTime(), filePath, ""
                        , Constant.item_audio_send
                        , seconds, "", "", "");

                //   ToastUtils.show(_mActivity, filePath);
                mList.add(msg);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mList.size() - 1);

                sendAudioToNet(msg);

            }
        });
    }

    private void sendAudioToNet(Msg msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileInputStream fis = null; // localfile为待上传的本地文件路径，需要指定到具体的文件名
                try {


                    fis = new FileInputStream(new File(msg.getLocalPath()));


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BucketStorageInfo storageInfo = obsClient.getBucketStorageInfo("t011t");

                Long bucketSize = storageInfo.getSize() / (1024 * 1024 * 1024);
                if (bucketSize < 50) {//桶容量小于50g，可以上传
                    String s = UUID.randomUUID().toString();
                    obsClient.putObject("t011t", s, fis);
                    //amr
                    msg.setNetPath(upOrDownPoint + s + ".amr");

                    Gson gson = new Gson();
                    String str = gson.toJson(new AllTypeMsg(null, null,
                            null, msg, Constant.msg_json));
                    OkHttpUtils.get()
                            .url(Constant.URL_netty + "sendFanoutMessage")
                            .addParams("userid", chatParce.yourUserId)
                            .addParams("contant", str)
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("ok")) {
                                ToastUtils.show(_mActivity, "net发送成功");
                            }
                        }
                    });

                }
            }
        }).start();

    }

    Handler handler = new Handler();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMore:


                if (SoftKeyboardUtils.isSoftShowing(_mActivity)) {

                    SoftKeyboardUtils.hideSystemSoftKeyboard(_mActivity);

                    if (bolck_titlebar.getVisibility() == View.GONE) {

                        handler.postDelayed(new Runnable() {
                            public void run() {
                                bolck_titlebar.setVisibility(View.VISIBLE);
                            }
                        }, 100);


                    } else {
                        bolck_titlebar.setVisibility(View.GONE);
                    }

                } else {
                    if (bolck_titlebar.getVisibility() == View.GONE) {
                        bolck_titlebar.setVisibility(View.VISIBLE);

                    } else {
                        bolck_titlebar.setVisibility(View.GONE);
                    }
                }


                break;
            case R.id.ivAlbum:


                startPictureSelector();


                break;
            case R.id.ivShot:

                extraTransaction()
//
                        .setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
                                R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
                        .start(FragmentTakeVidoe.newInstance());


                break;
            case R.id.ivLocation:



                startActivityForResult(new Intent(_mActivity, GaoDeMap.class), 9001);
                break;
            case R.id.ivVideo:

                startActivity(new Intent(_mActivity, LiveMain.class));
                break;
            case R.id.btnSend:


                if (!(mEditText.getText().toString().trim().equals(""))) {
                    Msg msg = new Msg(null, userid, chatParce.yourUserId, mEditText.getText().toString().trim(),
                            UUID.randomUUID().toString(), "", "", "s",
                            Constant.item_text_send, 0, "", "", "");

                    mList.add(msg);
                    App.getDaoSession().getMsgDao().insert(msg);
                    adapter.notifyDataSetChanged();
                    mEditText.setText("");
                    recyclerView.scrollToPosition(mList.size() - 1);

                    Gson gson = new Gson();
                    String str = gson.toJson(new AllTypeMsg(null, null,
                            null, msg, Constant.msg_json));
                    OkHttpUtils.get()
                            .url(Constant.URL_netty + "sendFanoutMessage")
                            .addParams("userid", chatParce.yourUserId)
                            .addParams("contant", str)
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("ok")) {
                                ToastUtils.show(_mActivity, "net发送成功");
                            }
                        }
                    });
                    EventBus.getDefault().post(new ChatTellRecementMsg(msg));
                }

                break;
            case R.id.ivEmo:

                recyclerView.scrollToPosition(mList.size() - 1);
                bolck_titlebar.setVisibility(View.GONE);
                emojiPopup.toggle();
                break;
            case R.id.etContent:

                recyclerView.scrollToPosition(mList.size() - 1);

                if (bolck_titlebar.getVisibility() == View.VISIBLE) {

                    bolck_titlebar.setVisibility(View.GONE);
                }
                break;
            case R.id.ivAudio:


                if (SoftKeyboardUtils.isSoftShowing(_mActivity)) {

                    SoftKeyboardUtils.hideSystemSoftKeyboard(_mActivity);
                    if (id_recorder_button.getVisibility() == View.GONE) {
                        emojiPopup.dismiss();
                        bolck_titlebar.setVisibility(View.GONE);
                        id_recorder_button.setVisibility(View.VISIBLE);
                        ivMore.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);//emoji开关
                        mEditText.setVisibility(View.GONE);

                    } else {
                        id_recorder_button.setVisibility(View.GONE);
                        ivMore.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);//emoji开关
                        mEditText.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (id_recorder_button.getVisibility() == View.GONE) {
                        emojiPopup.dismiss();
                        bolck_titlebar.setVisibility(View.GONE);
                        id_recorder_button.setVisibility(View.VISIBLE);
                        ivMore.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);//emoji开关
                        mEditText.setVisibility(View.GONE);

                    } else {
                        id_recorder_button.setVisibility(View.GONE);
                        ivMore.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);//emoji开关
                        mEditText.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.iv_test:
                startActivity(new Intent(_mActivity, GetLiveStream.class));
                break;

        }
    }


    private void startPictureSelector() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(_mActivity)
                .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(R.style.picture_WeChat_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .isUseCustomCamera(false)// 是否使用自定义相机
                .setLanguage(-1)// 设置语言，默认中文
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                //.setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //.setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                //.setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// 列表动画效果
                .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                // .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                //.setOutputCameraPath()// 自定义相机输出目录，只针对Android Q以下，例如 Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +  File.separator + "Camera" + File.separator;
                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                .maxSelectNum(9)// 最大图片选择数量
                .minSelectNum(0)// 最小选择数量
                .maxVideoSelectNum(9) // 视频最大选择数量
                .minVideoSelectNum(0)// 视频最小选择数量
                //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
                //.isAndroidQTransform(false)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true)// 是否可预览图片
                .isPreviewVideo(true)// 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                .isEnablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                // .isEnableCrop(true)// 是否裁剪
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                .isCompress(true)// 是否压缩
                //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                // .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                // .withAspectRatio(1 , 1 )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                // .isGif(true)// 是否显示gif图片
                // .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                //.circleDimmedLayer(true)// 是否圆形裁剪
                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                //  .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                //  .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                //  .isOpenClickSound(cb_voice.isChecked())// 是否开启点击声音
                //  .selectionData(mAdapter.getData())// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //.videoMinSecond(10)// 查询多少秒以内的视频
                //.videoMaxSecond(15)// 查询多少秒以内的视频
                //.recordVideoSecond(10)//录制视频秒数 默认60s
                //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                // .cutOutQuality(90)// 裁剪输出质量 默认100
                // .minimumCompressSize(100)// 小于多少kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled(false)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                .forResult(new OnResultCallbackListener() {
                    @Override
                    public void onResult(List result) {
                        // 结果回调
                        List<LocalMedia> selectList = result;
                        if (selectList.size() > 0) {


                            for (LocalMedia media : selectList) {
                                if (media.getDuration() > 0) {//视频文件
                                    mList.add(new Msg(null, userid, chatParce.yourUserId, "", UUID.randomUUID().toString(),
                                            TimeHelper.getCurrentTime(), media.getPath(),
                                            media.getPath(), Constant.item_video_send
                                            , 10, "", "", ""));
                                    imglist.add(mList.size() - 1);//建立图片位置和条目位置之间的关系
                                } else {
                                    mList.add(new Msg(null, userid, chatParce.yourUserId, "", UUID.randomUUID().toString(),
                                            TimeHelper.getCurrentTime(), media.getPath(),
                                            media.getPath(), Constant.item_image_send
                                            , 0, "", "", ""));
                                    imglist.add(mList.size() - 1);
                                }

                                //  ToastUtils.show(_mActivity, String.valueOf(media.getDuration()));
                            }
                            selectList1.addAll(selectList);
                            adapter.notifyDataSetChanged();
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mList.size() - 1);

                            List fileNetPathList = new ArrayList();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (LocalMedia media : selectList) {

                                        FileInputStream fis = null; // localfile为待上传的本地文件路径，需要指定到具体的文件名
                                        try {

                                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                fis = new FileInputStream(new File(media.getAndroidQToPath()));
                                            } else {
                                                fis = new FileInputStream(new File(media.getPath()));

                                            }

                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                        BucketStorageInfo storageInfo = obsClient.getBucketStorageInfo("t011t");

                                        Long bucketSize = storageInfo.getSize() / (1024 * 1024 * 1024);
                                        if (bucketSize < 50) {//桶容量小于50g，可以上传
                                            obsClient.putObject("t011t", media.getFileName(), fis);
                                            if (media.getDuration() > 0) {//视频文件
                                                Msg msg = new Msg(null, userid, chatParce.yourUserId, "", UUID.randomUUID().toString(),
                                                        TimeHelper.getCurrentTime(), media.getPath(), upOrDownPoint + media.getFileName()
                                                        , Constant.item_video_send
                                                        , 0, "", "", "");


                                                Gson gson = new Gson();
                                                String str = gson.toJson(new AllTypeMsg(null, null,
                                                        null, msg, Constant.msg_json));
                                                OkHttpUtils.get()
                                                        .url(Constant.URL_netty + "sendFanoutMessage")
                                                        .addParams("userid", chatParce.yourUserId)
                                                        .addParams("contant", str)
                                                        .build().execute(new StringCallback() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {

                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        if (response.equals("ok")) {
                                                            ToastUtils.show(_mActivity, "net发送成功");
                                                        }
                                                    }
                                                });
                                                EventBus.getDefault().post(new ChatTellRecementMsg(msg));
                                            } else {
                                                Msg msg = new Msg(null, userid, chatParce.yourUserId, "", UUID.randomUUID().toString(),
                                                        TimeHelper.getCurrentTime(), media.getPath(), upOrDownPoint + media.getFileName()
                                                        , Constant.item_image_send
                                                        , 0, "", "", "");

                                                Gson gson = new Gson();
                                                String str = gson.toJson(new AllTypeMsg(null, null,
                                                        null, msg, Constant.msg_json));
                                                OkHttpUtils.get()
                                                        .url(Constant.URL_netty + "sendFanoutMessage")
                                                        .addParams("userid", chatParce.yourUserId)
                                                        .addParams("contant", str)
                                                        .build().execute(new StringCallback() {
                                                    @Override
                                                    public void onError(Call call, Exception e, int id) {

                                                    }

                                                    @Override
                                                    public void onResponse(String response, int id) {
                                                        if (response.equals("ok")) {
                                                            ToastUtils.show(_mActivity, "net发送成功");
                                                        }
                                                    }
                                                });
                                                EventBus.getDefault().post(new ChatTellRecementMsg(msg));

                                            }

                                        }
                                    }

                                }
                            }).start();

                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });//结果回调onActivityResult code

    }

    private void startPictureSelector2(int requestcode) {

        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(_mActivity)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(R.style.picture_WeChat_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .isUseCustomCamera(false)// 是否使用自定义相机
                .setLanguage(-1)// 设置语言，默认中文
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                //.setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //.setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                //.setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// 列表动画效果
                .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                // .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                //.setOutputCameraPath()// 自定义相机输出目录，只针对Android Q以下，例如 Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +  File.separator + "Camera" + File.separator;
                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .maxVideoSelectNum(1) // 视频最大选择数量
                //.minVideoSelectNum(1)// 视频最小选择数量
                //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
                //.isAndroidQTransform(false)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true)// 是否可预览图片
                .isPreviewVideo(true)// 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                .isEnablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                // .isEnableCrop(true)// 是否裁剪
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                // .isCompress(true)// 是否压缩
                //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                // .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                // .withAspectRatio(1 , 1 )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                // .isGif(true)// 是否显示gif图片
                // .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                //.circleDimmedLayer(true)// 是否圆形裁剪
                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                //  .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                //  .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                //  .isOpenClickSound(cb_voice.isChecked())// 是否开启点击声音
                //  .selectionData(mAdapter.getData())// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //.videoMinSecond(10)// 查询多少秒以内的视频
                //.videoMaxSecond(15)// 查询多少秒以内的视频
                //.recordVideoSecond(10)//录制视频秒数 默认60s
                //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                // .cutOutQuality(90)// 裁剪输出质量 默认100
                // .minimumCompressSize(100)// 小于多少kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled(false)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                .forResult(new OnResultCallbackListener() {
                    @Override
                    public void onResult(List result) {

                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.show(_mActivity, "点击了取消");

                    }
                });//结果回调onActivityResult code

    }

    private void startTakePhoto() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(_mActivity)
                //  .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(R.style.picture_WeChat_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .isUseCustomCamera(false)// 是否使用自定义相机
                .setLanguage(-1)// 设置语言，默认中文
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                //.setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //.setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                //.setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)// 列表动画效果
                .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                // .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                //.setOutputCameraPath()// 自定义相机输出目录，只针对Android Q以下，例如 Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +  File.separator + "Camera" + File.separator;
                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                .maxSelectNum(9)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .maxVideoSelectNum(1) // 视频最大选择数量
                //.minVideoSelectNum(1)// 视频最小选择数量
                //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 如果视频有旋转角度则对换宽高,默认为false
                //.isAndroidQTransform(false)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .isSingleDirectReturn(true)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isPreviewImage(true)// 是否可预览图片
                .isPreviewVideo(true)// 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                .isEnablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                // .isEnableCrop(true)// 是否裁剪
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                // .isCompress(true)// 是否压缩
                //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                // .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                // .withAspectRatio(1 , 1 )// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //.hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                // .isGif(true)// 是否显示gif图片
                // .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                //.circleDimmedLayer(true)// 是否圆形裁剪
                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                //  .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                //  .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                //  .isOpenClickSound(cb_voice.isChecked())// 是否开启点击声音
                //  .selectionData(mAdapter.getData())// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //.videoMinSecond(10)// 查询多少秒以内的视频
                //.videoMaxSecond(15)// 查询多少秒以内的视频
                //.recordVideoSecond(10)//录制视频秒数 默认60s
                //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                // .cutOutQuality(90)// 裁剪输出质量 默认100
                // .minimumCompressSize(100)// 小于多少kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(false) // 裁剪是否可旋转图片
                //.scaleEnabled(false)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    Drawable d1 = Drawable.createFromPath(selectList.get(0).getRealPath());
                    llrootView.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(selectList.get(0).getRealPath())));
                    break;
                case 9001:
                    //地图ac返回的，经纬度
                    String l = data.getStringExtra("L");
                    String a = data.getStringExtra("A");

                    if (a != null) {
                        Msg msg = new Msg(null, userid, chatParce.yourUserId, l + "|" + a,
                                UUID.randomUUID().toString(), "", "", "s",
                                Constant.item_location_send, 0, "", "", "");

                        mList.add(msg);
                        App.getDaoSession().getMsgDao().insert(msg);
                        adapter.notifyDataSetChanged();
                        mEditText.setText("");
                        recyclerView.scrollToPosition(mList.size() - 1);

                        Gson gson = new Gson();
                        String str = gson.toJson(new AllTypeMsg(null, null,
                                null, msg, Constant.msg_json));
                        OkHttpUtils.get()
                                .url(Constant.URL_netty + "sendFanoutMessage")
                                .addParams("userid", chatParce.yourUserId)
                                .addParams("contant", str)
                                .build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (response.equals("ok")) {
                                    ToastUtils.show(_mActivity, "net发送成功");
                                }
                            }
                        });
                        EventBus.getDefault().post(new ChatTellRecementMsg(msg));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaManager.release();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void getUrl1(UrlMsg urlMsg) {
        //视频文件
        mList.add(new Msg(null, userid, chatParce.yourUserId, "", UUID.randomUUID().toString(),
                TimeHelper.getCurrentTime(), urlMsg.getUrl(), urlMsg.getUrl()
                , Constant.item_video_send
                , 0, "", "", ""));
        imglist.add(mList.size() - 1);//建立图片位置和条目位置之间的关系
        selectList1.add(new LocalMedia(urlMsg.getUrl(), 10,
                PictureSelectionConfig.getInstance().chooseMode, PictureMimeType.MIME_TYPE_VIDEO));
        adapter.notifyDataSetChanged();
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mList.size() - 1);

        FileInputStream fis = null; // localfile为待上传的本地文件路径，需要指定到具体的文件名
        try {


            fis = new FileInputStream(new File(urlMsg.getUrl()));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BucketStorageInfo storageInfo = obsClient.getBucketStorageInfo("t011t");

        Long bucketSize = storageInfo.getSize() / (1024 * 1024 * 1024);
        if (bucketSize < 50) {//桶容量小于50g，可以上传
            String s = UUID.randomUUID().toString();
            obsClient.putObject("t011t", s, fis);

            Msg msg = new Msg(null, userid, chatParce.yourUserId, "", UUID.randomUUID().toString(),
                    TimeHelper.getCurrentTime(), urlMsg.getUrl(), upOrDownPoint + s + ".mp4"
                    , Constant.item_video_send
                    , 0, "", "", "");


            Gson gson = new Gson();
            String str = gson.toJson(new AllTypeMsg(null, null,
                    null, msg, Constant.msg_json));
            OkHttpUtils.get()
                    .url(Constant.URL_netty + "sendFanoutMessage")
                    .addParams("userid", chatParce.yourUserId)
                    .addParams("contant", str)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    if (response.equals("ok")) {
                        ToastUtils.show(_mActivity, "net发送成功");
                    }
                }
            });

        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)//
    public void getMsgBus(MsgBus msgbus) {
        Msg msg = msgbus.getMsg();
        ToastUtils.show(_mActivity, "收到了来自" + msg.getSendID() + "的消息，内容是" +
                msg.getMsg());
        //接收者要对消息进行改装并存储

        mList.add(changeMsg(msg));

        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mList.size() - 1);

    }

    private Msg changeMsg(Msg msg) {
        Msg msg1 = new Msg();
        switch (msg.getContentType()) {
            case Constant.item_text_receive:
               msg1 =msg;
                break;
            case Constant.item_audio_receive:
                msg1 =msg;
                break;
            case Constant.item_image_receive:
                msg1 =msg;
                imglist.add(mList.size() - 1);
                selectList1.add(new LocalMedia(msg.getNetPath(), 0,
                        PictureSelectionConfig.getInstance().chooseMode, PictureMimeType.MIME_TYPE_IMAGE));


                break;
            case Constant.item_video_receive:
                msg1 =msg;
                selectList1.add(new LocalMedia(msg.getNetPath(), 10,
                        PictureSelectionConfig.getInstance().chooseMode, PictureMimeType.MIME_TYPE_VIDEO));

                imglist.add(mList.size() - 1);

                break;
            case Constant.item_location_receive:
                msg1 =msg;
                break;
        }
        return msg1;
    }

}