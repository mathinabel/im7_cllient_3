package highperformancefriendscircle.others;

import android.content.Context;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quyuanjin.imseven.App;
import com.quyuanjin.imseven.Constant;
import com.quyuanjin.imseven.utils.SharedPreferencesUtils;
import com.quyuanjin.imseven.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import highperformancefriendscircle.Constants;
import highperformancefriendscircle.beans.CommentBean;
import highperformancefriendscircle.beans.FriendCircleBean;
import highperformancefriendscircle.beans.OtherInfoBean;
import highperformancefriendscircle.beans.PraiseBean;
import highperformancefriendscircle.beans.UserBean;
import highperformancefriendscircle.beans.emoji.EmojiBean;
import highperformancefriendscircle.beans.emoji.EmojiDataSource;
import highperformancefriendscircle.utils.SpanUtils;
import okhttp3.Call;


/**
 * @author KCrason
 * @date 2018/5/2
 */
public class DataCenter {

    private static String userid;

    public static void init() {
        new Thread(DataCenter::loadEmojis).start();
    }

    public static final List<EmojiDataSource> emojiDataSources = new ArrayList<>();

    public static void loadEmojis() {
        for (int i = 0; i < 2; i++) {
            EmojiDataSource emojiDataSource = new EmojiDataSource();
            List<EmojiBean> typeEmojiBeans = new ArrayList<>();
            if (i == 0) {
                for (int j = 0; j < Constants.TYPE01_EMOJI_NAME.length; j++) {
                    EmojiBean emojiBean = new EmojiBean();
                    emojiBean.setEmojiName(Constants.TYPE01_EMOJI_NAME[j]);
                    emojiBean.setEmojiResource(Constants.TYPE01_EMOJI_DREWABLES[j]);
                    typeEmojiBeans.add(emojiBean);
                }
                emojiDataSource.setEmojiType(Constants.EmojiType.EMOJI_TYPE_01);
            } else {
                for (int j = 0; j < Constants.TYPE02_EMOJI_NAME.length; j++) {
                    EmojiBean emojiBean = new EmojiBean();
                    emojiBean.setEmojiName(Constants.TYPE02_EMOJI_NAME[j]);
                    emojiBean.setEmojiResource(Constants.TYPE02_EMOJI_DREWABLES[j]);
                    typeEmojiBeans.add(emojiBean);
                }
                emojiDataSource.setEmojiType(Constants.EmojiType.EMOJI_TYPE_02);
            }
            emojiDataSource.setEmojiList(typeEmojiBeans);
            emojiDataSources.add(emojiDataSource);
        }
    }

    public static List<FriendCircleBean> makeFriendCircleBeans(Context context) {
        userid = (String) SharedPreferencesUtils.getParam(App.sContext, "userid", "");

        List<FriendCircleBean> friendCircleBeans = new ArrayList<FriendCircleBean>();
//         for (int i = 0; i < 1000; i++) {
//
//            FriendCircleBean friendCircleBean = new FriendCircleBean();
//            int randomValue = (int) (Math.random() * 300);
//            if (randomValue < 100) {
//              //  friendCircleBean.setViewType(Constants.FriendCircleType.FRIEND_CIRCLE_TYPE_ONLY_WORD);
//            } else if (randomValue < 200) {
//              //  friendCircleBean.setViewType(Constants.FriendCircleType.FRIEND_CIRCLE_TYPE_WORD_AND_IMAGES);
//            } else {
//               // friendCircleBean.setViewType(Constants.FriendCircleType.FRIEND_CIRCLE_TYPE_WORD_AND_URL);
//            }
//           // friendCircleBean.setCommentBeans(makeCommentBeans(context));
//           // friendCircleBean.setImageUrls(makeImages());
//           // List<PraiseBean> praiseBeans = makePraiseBeans();
//         //   friendCircleBean.setPraiseSpan(SpanUtils.makePraiseSpan(context, praiseBeans));
//          //  friendCircleBean.setPraiseBeans(praiseBeans);
//            friendCircleBean.setContent(Constants.CONTENT[(int) (Math.random() * 10)]);
//
//            UserBean userBean = new UserBean();
//            userBean.setUserName(Constants.USER_NAME[(int) (Math.random() * 30)]);
//            userBean.setUserAvatarUrl(Constants.IMAGE_URL[(int) (Math.random() * 50)]);
//           // friendCircleBean.setUserBean(userBean);
//
//
//            OtherInfoBean otherInfoBean = new OtherInfoBean();
//            otherInfoBean.setTime(Constants.TIMES[(int) (Math.random() * 20)]);
//            int random = (int) (Math.random() * 30);
//            if (random < 20) {
//                otherInfoBean.setSource(Constants.SOURCE[random]);
//            } else {
//                otherInfoBean.setSource("");
//            }
//          //  friendCircleBean.setOtherInfoBean(otherInfoBean);
//            friendCircleBeans.add(friendCircleBean);
//
//        } return friendCircleBeans;

//        OkHttpUtils.get()
//                .url(Constant.URL_netty_friendCircle + "getFriendCircle")
//                .addParams("userid", userid)
//                //.addParams("contant", )
//                .build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//
//                ToastUtils.show(App.sContext, response);
//            }
//        });
//        OkHttpUtils.get()
//                .url(Constant.URL_netty_friendCircle + "getFirendCircleDetailByFUUID")
//                .addParams("fuuids", fuuid)
//                //.addParams("contant", )
//                .build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//
//                ToastUtils.show(App.sContext, response);
//            }
//        });
        final CountDownLatch latch= new CountDownLatch(0);
        OkHttpUtils.get()
                .url(Constant.URL_netty_friendCircle + "getFriendCircleBeans")
                .addParams("userid", userid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.show(App.sContext, "哪里出错了");

            }

            @Override
            public void onResponse(String response, int id) {

                ToastUtils.show(App.sContext, response);
                Gson gson = new Gson();
                List<FriendCircleBean> fcbs = gson.fromJson(response, new TypeToken<List<FriendCircleBean>>() {
                }.getType());
                friendCircleBeans.addAll(fcbs);
                latch.countDown();
            }
        });
        return friendCircleBeans;

    }


    private static List<String> makeImages() {
        List<String> imageBeans = new ArrayList<>();
        int randomCount = (int) (Math.random() * 9);
        if (randomCount == 0) {
            randomCount = randomCount + 1;
        } else if (randomCount == 8) {
            randomCount = randomCount + 1;
        }
        for (int i = 0; i < randomCount; i++) {
            imageBeans.add(Constants.IMAGE_URL[(int) (Math.random() * 50)]);
        }
        return imageBeans;
    }


    private static List<PraiseBean> makePraiseBeans() {
        List<PraiseBean> praiseBeans = new ArrayList<>();
        int randomCount = (int) (Math.random() * 20);
        for (int i = 0; i < randomCount; i++) {
            PraiseBean praiseBean = new PraiseBean();
            praiseBean.setPraiseUserName(Constants.USER_NAME[(int) (Math.random() * 30)]);
            praiseBeans.add(praiseBean);
        }
        return praiseBeans;
    }


    private static List<CommentBean> makeCommentBeans(Context context) {
        List<CommentBean> commentBeans = new ArrayList<>();
        int randomCount = (int) (Math.random() * 20);
        for (int i = 0; i < randomCount; i++) {
            CommentBean commentBean = new CommentBean();
            if ((int) (Math.random() * 100) % 2 == 0) {
                commentBean.setCommentType(Constants.CommentType.COMMENT_TYPE_SINGLE);
                commentBean.setChildUserName(Constants.USER_NAME[(int) (Math.random() * 30)]);
            } else {
                commentBean.setCommentType(Constants.CommentType.COMMENT_TYPE_REPLY);
                commentBean.setChildUserName(Constants.USER_NAME[(int) (Math.random() * 30)]);
                commentBean.setParentUserName(Constants.USER_NAME[(int) (Math.random() * 30)]);
            }

            commentBean.setCommentContent(Constants.COMMENT_CONTENT[(int) (Math.random() * 30)]);
            commentBean.build(context);
            commentBeans.add(commentBean);
        }
        return commentBeans;
    }
}
