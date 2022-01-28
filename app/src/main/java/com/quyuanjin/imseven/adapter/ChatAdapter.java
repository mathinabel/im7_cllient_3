package com.quyuanjin.imseven.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.quyuanjin.imseven.R;
import com.quyuanjin.imseven.pojo.Msg;
import com.quyuanjin.imseven.utils.BubbleImageView;
import com.quyuanjin.imseven.utils.pictureselect.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<Msg> arrayList;
    private Context context;
    private List<LocalMedia> selectList;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemVoiceClickListener onItemVoiceClickListener;
    private OnItemVoiceClickListener1 onItemVoiceClickListener1;
    private OnItemImageHeadClickListener onItemImageHeadClickListener;
    private String s;
    private PictureParameterStyle mPictureParameterStyle;
    private ArrayList<Integer> imglist;

    //设置回调接口
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemClickLitener) {
        this.onItemLongClickListener = mOnItemClickLitener;

    }

    public interface OnItemVoiceClickListener {
        void onItemVoiceClick(View view, int position);
    }

    public interface OnItemVoiceClickListener1 {
        void onItemVoiceClick1(View view, int position);
    }

    public interface OnItemImageHeadClickListener {
        void onItemImageHeadClick(View view, int position);
    }

    public void setOnItemImageHeadClickListener(OnItemImageHeadClickListener onItemImageHeadClickListener) {
        this.onItemImageHeadClickListener = onItemImageHeadClickListener;

    }

    public void setOnItemVoiceClickListener(OnItemVoiceClickListener onItemVoiceClickListener) {
        this.onItemVoiceClickListener = onItemVoiceClickListener;

    }

    public void setOnItemVoiceClickListener1(OnItemVoiceClickListener1 onItemVoiceClickListener1) {
        this.onItemVoiceClickListener1 = onItemVoiceClickListener1;

    }

    public ChatAdapter(Context context, ArrayList<Msg> arrayList, List<LocalMedia> selectList1, ArrayList<Integer> imglist) {
        this.arrayList = arrayList;
        this.context = context;
        this.selectList = selectList1;
        this.imglist = imglist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  s = (String) SharedPreferencesUtils.getParam(parent.getContext().getApplicationContext(), "CutPath", "");

        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_text_receive, parent, false);

            return new ViewHolder(view);

        } else if (viewType == 1) {
            View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_text_send, parent, false);
            return new ViewHolder1(view1);
        } else if (viewType == 2) {
            View view2 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_image_receive, parent, false);
            return new ViewHolder2(view2);
        } else if (viewType == 3) {
            View view3 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_image_send, parent, false);
            return new ViewHolder3(view3);
        } else if (viewType == 4) {
            View view4 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_audio_receive, parent, false);
            return new ViewHolder4(view4);
        } else if (viewType == 5) {
            View view5 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_audio_send, parent, false);
            return new ViewHolder5(view5);
        } else if (viewType == 6) {
            View view6 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video_receive, parent, false);
            return new ViewHolder6(view6);
        } else if (viewType == 7) {
            View view7 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video_send, parent, false);
            return new ViewHolder7(view7);
        } else if (viewType == 8) {
            View view8 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_location_receive, parent, false);
            return new ViewHolder8(view8);
        } else if (viewType == 9) {
            View view9 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_location_send, parent, false);
            return new ViewHolder9(view9);
        } else if (viewType == 10) {
            View view10 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_location_receive, parent, false);
            return new ViewHolder10(view10);
        } else if (viewType == 11) {
            View view11 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_location_receive, parent, false);
            return new ViewHolder11(view11);
        } else if (viewType == 12) {
            View view12 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_location_receive, parent, false);
            return new ViewHolder12(view12);
        } else {
            View view1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_location_send, parent, false);
            return new ViewHolder1(view1);
        }
    }

    //实际的phonecall 在新开的ac里面实现了，这里没用
    private RequestOptions mOptions = new RequestOptions()
            .centerCrop()
            .format(DecodeFormat.PREFER_RGB_565)
            .placeholder(R.mipmap.default_img);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {
            case 0://收到消息
                //头像点击事件
                ((ViewHolder) holder).simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemImageHeadClickListener.onItemImageHeadClick(view, position);
                    }
                });
                ((ViewHolder) holder).simpleDraweeView.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));
                ((ViewHolder) holder).textView.setText(arrayList.get(position).getMsg());

                //通过为条目设置点击事件触发回调
                if (onItemLongClickListener != null) {
                    ((ViewHolder) holder).textView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            onItemLongClickListener.onItemLongClick(view, position);
                            return false;
                        }
                    });
                }

                if (!"".equals(arrayList.get(position).getFanyi())) {
                    ((ViewHolder) holder).fanyi.setVisibility(View.VISIBLE);
                    ((ViewHolder) holder).fanyi.setText(arrayList.get(position).getFanyi());

                }
                break;
            case 1://发送消息
                //头像点击事件
                ((ViewHolder1) holder).simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                ((ViewHolder1) holder).textView.setText(arrayList.get(position).getMsg());
            /*    if (!("".equals(s))) {
                    ((ViewHolder1) holder).simpleDraweeView.setImageURI(Uri.parse(s));
                } else {
                    ((ViewHolder1) holder).simpleDraweeView.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));
                }*/

                //通过为条目设置点击事件触发回调
                if (onItemLongClickListener != null) {
                    ((ViewHolder1) holder).textView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            onItemLongClickListener.onItemLongClick(view, position);
                            return false;
                        }
                    });
                }
                if (!"".equals(arrayList.get(position).getFanyi())) {
                    ((ViewHolder1) holder).fanyi.setVisibility(View.VISIBLE);
                    ((ViewHolder1) holder).fanyi.setText(arrayList.get(position).getFanyi());

                }
                break;


            case 2://picture received
                //头像点击事件
                ((ViewHolder2) holder).imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                Glide.with(((ViewHolder2) holder).bubbleImageView.getContext()).
                        load(Uri.parse(arrayList.get(position).getNetPath())).apply(mOptions).into(((ViewHolder2) holder).bubbleImageView);
                ((ViewHolder2) holder).bubbleImageView.setProgressVisible(false);
                ((ViewHolder2) holder).bubbleImageView.showShadow(false);
                //   ((ViewHolder2) holder).imageView.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));
                ((ViewHolder2) holder).bubbleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PictureSelector.create((Activity) context)

                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                // .compressQuality(10)
                                .openExternalPreview(imglist.indexOf(position - 1), selectList);

                        //点击之后就下载该图片并修改数据库的本地存储位置存储


                    }
                });
                break;
            case 3:
                //头像点击事件
                ((ViewHolder3) holder).imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                Glide.with(((ViewHolder3) holder).bubbleImageView.getContext()).
                        load(Uri.fromFile(new File(arrayList.get(position).getLocalPath()))).apply(mOptions).into(((ViewHolder3) holder).bubbleImageView);

                ((ViewHolder3) holder).bubbleImageView.setProgressVisible(false);
                ((ViewHolder3) holder).bubbleImageView.showShadow(false);

                ((ViewHolder3) holder).bubbleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PictureSelector.create((Activity) context)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(imglist.indexOf(position), selectList);

                    }
                });
//                if (!("".equals(s))) {
//                    ((ViewHolder3) holder).imageView.setImageURI(Uri.parse(s));
//                } else {
//                    ((ViewHolder3) holder).imageView.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));
//                }
                break;
            case 4://voice received
                //头像点击事件
                ((ViewHolder4) holder).my_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                ((ViewHolder4) holder).llAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemVoiceClickListener1.onItemVoiceClick1(view, position);
                    }
                });
                ((ViewHolder4) holder).tvDuration.setText(String.valueOf((int) arrayList.get(position).getRecorderTime()));
                ((ViewHolder4) holder).my_image_view.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));

                break;
            case 5:
                //头像点击事件
                ((ViewHolder5) holder).my_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                ((ViewHolder5) holder).rlAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemVoiceClickListener.onItemVoiceClick(view, position);
                    }
                });

                ((ViewHolder5) holder).tvDuration.setText(String.valueOf((int) arrayList.get(position).getRecorderTime()));
            /*    if (!("".equals(s))) {
                    ((ViewHolder5) holder).my_image_view.setImageURI(Uri.parse(s));
                } else {
                    ((ViewHolder5) holder).my_image_view.setImageURI(Uri.parse(arrayList.get(position).getNetPath()));
                }*/
                break;
            case 6://video
                //头像点击事件
                ((ViewHolder6) holder).imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                Glide.with(((ViewHolder6) holder).bubbleImageView.getContext()).
                        load(Uri.parse(arrayList.get(position).getNetPath())).apply(mOptions).into(((ViewHolder6) holder).bubbleImageView);

                ((ViewHolder6) holder).bubbleImageView.setProgressVisible(false);
                ((ViewHolder6) holder).bubbleImageView.showShadow(false);

                ((ViewHolder6) holder).bubbleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        PictureSelector.create((Activity) context)
//                                .externalPictureVideo(arrayList.get(position).getNetPath());
                        PictureSelector.create((Activity) context)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(imglist.indexOf(position), selectList);
                    }
                });
                break;
            case 7://video_send
                //头像点击事件
                ((ViewHolder7) holder).imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                Glide.with(((ViewHolder7) holder).bubbleImageView.getContext()).
                        load(Uri.fromFile(new File(arrayList.get(position).getLocalPath()))).apply(mOptions).into(((ViewHolder7) holder).bubbleImageView);

                ((ViewHolder7) holder).bubbleImageView.setProgressVisible(false);
                ((ViewHolder7) holder).bubbleImageView.showShadow(false);

                ((ViewHolder7) holder).bubbleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PictureSelector.create((Activity) context)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(imglist.indexOf(position), selectList);
                    }
                });
                break;
            case 8://location rec

                break;
            case 9://location send

                break;
            case 10://item_phonecall_receive

                break;
            case 11://item_phonecall_send

                break;
            case 12:

                break;
        }
    }

    public Msg getMsg(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {


        if (arrayList.get(position).getContentType() == (0)) {
            return 0;
        } else if (arrayList.get(position).getContentType() == (1)) {
            return 1;
        } else if (arrayList.get(position).getContentType() == (2)) {

            return 2;
        } else if (arrayList.get(position).getContentType() == (3)) {
            return 3;
        } else if (arrayList.get(position).getContentType() == (4)) {
            return 4;
        } else if (arrayList.get(position).getContentType() == (5)) {
            return 5;
        } else if (arrayList.get(position).getContentType() == (6)) {
            return 6;
        } else if (arrayList.get(position).getContentType() == (7)) {
            return 7;
        } else if (arrayList.get(position).getContentType() == (8)) {
            return 8;
        } else if (arrayList.get(position).getContentType() == (9)) {
            return 9;
        } else if (arrayList.get(position).getContentType() == (10)) {
            return 10;
        } else if (arrayList.get(position).getContentType() == (11)) {
            return 11;
        } else {
            return 12;
        }


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        SimpleDraweeView simpleDraweeView;
        TextView fanyi;

        public ViewHolder(View view) {
            super(view);
            simpleDraweeView = view.findViewById(R.id.my_image_view);
            textView = view.findViewById(R.id.tvText);
            fanyi = view.findViewById(R.id.fanyineirong);
        }
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {

        TextView textView;
        SimpleDraweeView simpleDraweeView;
        TextView fanyi;

        public ViewHolder1(View view) {
            super(view);
            textView = view.findViewById(R.id.tvText);
            simpleDraweeView = view.findViewById(R.id.my_image_view);
            fanyi = view.findViewById(R.id.fanyineirong);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {

        BubbleImageView bubbleImageView;
        SimpleDraweeView imageView;

        public ViewHolder2(View view) {
            super(view);
            bubbleImageView = view.findViewById(R.id.bivPic11);
            imageView = view.findViewById(R.id.ivAvatar11);

        }
    }

    static class ViewHolder3 extends RecyclerView.ViewHolder {
        BubbleImageView bubbleImageView;
        SimpleDraweeView imageView;

        public ViewHolder3(View view) {
            super(view);
            bubbleImageView = view.findViewById(R.id.bivPic12);
            imageView = view.findViewById(R.id.ivAvatar12);
        }
    }

    static class ViewHolder4 extends RecyclerView.ViewHolder {
        TextView tvDuration;
        RelativeLayout llAudio;
        SimpleDraweeView my_image_view;

        public ViewHolder4(View view) {
            super(view);
            tvDuration = view.findViewById(R.id.tvDuration11);
            llAudio = view.findViewById(R.id.llAudio);
            my_image_view = view.findViewById(R.id.my_image_view1);
        }
    }

    static class ViewHolder5 extends RecyclerView.ViewHolder {
        TextView tvDuration;
        RelativeLayout rlAudio;
        SimpleDraweeView my_image_view;

        public ViewHolder5(View view) {
            super(view);

            tvDuration = view.findViewById(R.id.tvDuration);
            rlAudio = view.findViewById(R.id.rlAudio);
            my_image_view = view.findViewById(R.id.my_image_view2);
        }
    }

    static class ViewHolder6 extends RecyclerView.ViewHolder {
        BubbleImageView bubbleImageView;
        SimpleDraweeView imageView;

        public ViewHolder6(View view) {
            super(view);
            bubbleImageView = view.findViewById(R.id.bivPic6);
            imageView = view.findViewById(R.id.ivAvatara6);

        }
    }

    static class ViewHolder7 extends RecyclerView.ViewHolder {

        BubbleImageView bubbleImageView;
        SimpleDraweeView imageView;

        public ViewHolder7(View view) {
            super(view);
            bubbleImageView = view.findViewById(R.id.bivPica1);
            imageView = view.findViewById(R.id.ivAvatara1);

        }
    }

    static class ViewHolder8 extends RecyclerView.ViewHolder {


        public ViewHolder8(View view) {
            super(view);


        }
    }

    static class ViewHolder9 extends RecyclerView.ViewHolder {


        public ViewHolder9(View view) {
            super(view);


        }
    }

    static class ViewHolder10 extends RecyclerView.ViewHolder {


        public ViewHolder10(View view) {
            super(view);


        }
    }

    static class ViewHolder11 extends RecyclerView.ViewHolder {


        public ViewHolder11(View view) {
            super(view);


        }
    }

    static class ViewHolder12 extends RecyclerView.ViewHolder {


        public ViewHolder12(View view) {
            super(view);


        }
    }

    private void getDefaultStyle() {
        // 相册主题
        mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = false;
        // 相册状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // 相册父容器背景色
        mPictureParameterStyle.pictureContainerBackgroundColor = ContextCompat.getColor(context, R.color.app_color_black);
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 选择相册目录背景样式
        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg;
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(context, R.color.picture_color_grey);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(context, R.color.picture_color_fa632d);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(context, R.color.picture_color_fa632d);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(context, R.color.picture_color_white);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(context, R.color.picture_color_grey);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(context, R.color.app_color_white);
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");
//        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureRightDefaultText = "";
//        // 自定义相册未完成文本内容
//        mPictureParameterStyle.pictureUnCompleteText = "";
//        // 自定义相册完成文本内容
//        mPictureParameterStyle.pictureCompleteText = "";
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";
//
//        // 自定义相册标题字体大小
//        mPictureParameterStyle.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 14;


    }
}
