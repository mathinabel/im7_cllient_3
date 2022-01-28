package com.quyuanjin.imseven;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.textservice.TextInfo;


import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.kongzue.dialog.util.DialogSettings;
import com.luck.picture.lib.app.IApp;
import com.luck.picture.lib.engine.PictureSelectorEngine;
import com.quyuanjin.imseven.pojo.DaoMaster;
import com.quyuanjin.imseven.pojo.DaoSession;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;


import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import highperformancefriendscircle.others.DataCenter;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;
import okhttp3.OkHttpClient;

/**
 * Created by YoKey on 16/11/23.
 */
public class App extends Application implements CameraXConfig.Provider {
    private static DaoSession daoSession;
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new IosEmojiProvider());
        Fresco.initialize(this);
        setFragmentMode();
        initGreenDao();
        initOkhttpHelp();
        dialogSetting();
        sContext = getApplicationContext();
        DataCenter.init();

    }

    private void dialogSetting() {
        DialogSettings.isUseBlur = (false);                   //是否开启模糊效果，默认关闭
        DialogSettings.modalDialog = (true);                 //是否开启模态窗口模式，一次显示多个对话框将以队列形式一个一个显示，默认关闭
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;          //全局主题风格，提供三种可选风格，STYLE_MATERIAL, STYLE_KONGZUE, STYLE_IOS
        DialogSettings.theme = DialogSettings.THEME.LIGHT;          //全局对话框明暗风格，提供两种可选主题，LIGHT, DARK
        DialogSettings.tipTheme = DialogSettings.THEME.LIGHT;       //全局提示框明暗风格，提供两种可选主题，LIGHT, DARK
  /*      DialogSettings.titleTextInfo = TextInfo;              //全局对话框标题文字样式
        DialogSettings.menuTitleInfo = (TextInfo);              //全局菜单标题文字样式
        DialogSettings.menuTextInfo = (TextInfo);               //全局菜单列表文字样式
        DialogSettings.contentTextInfo = (TextInfo);            //全局正文文字样式
        DialogSettings.buttonTextInfo = (TextInfo);             //全局默认按钮文字样式
        DialogSettings.buttonPositiveTextInfo = (TextInfo);     //全局焦点按钮文字样式（一般指确定按钮）
        DialogSettings.inputInfo = (InputInfo);                 //全局输入框文本样式
        DialogSettings.backgroundColor = (ColorInt);            //全局对话框背景颜色，值0时不生效
        DialogSettings.cancelable = (boolean);                  //全局对话框默认是否可以点击外围遮罩区域或返回键关闭，此开关不影响提示框（TipDialog）以及等待框（TipDialog）
        DialogSettings.cancelableTipDialog = (boolean);         //全局提示框及等待框（WaitDialog、TipDialog）默认是否可以关闭
        DialogSettings.DEBUGMODE = (boolean);                   //是否允许打印日志
        DialogSettings.blurAlpha = (int);                       //开启模糊后的透明度（0~255）
        DialogSettings.systemDialogStyle = (styleResId);        //自定义系统对话框style，注意设置此功能会导致原对话框风格和动画失效
        DialogSettings.dialogLifeCycleListener = (DialogLifeCycleListener);  //全局Dialog生命周期监听器
        DialogSettings.defaultCancelButtonText = (String);      //设置 BottomMenu 和 ShareDialog 默认“取消”按钮的文字
        DialogSettings.tipBackgroundResId = (drawableResId);    //设置 TipDialog 和 WaitDialog 的背景资源
        DialogSettings.tipTextInfo = (InputInfo);               //设置 TipDialog 和 WaitDialog 文字样式
        DialogSettings.autoShowInputKeyboard = (boolean);       //设置 InputDialog 是否自动弹出输入法
        DialogSettings.okButtonDrawable = (drawable);           //设置确定按钮背景资源
        DialogSettings.cancelButtonDrawable = (drawable);       //设置取消按钮背景资源
        DialogSettings.otherButtonDrawable = (drawable);        //设置其他按钮背景资源
*/
//检查 Renderscript 兼容性，若设备不支持，DialogSettings.isUseBlur 会自动关闭；
        //  DialogSettings.checkRenderscriptSupport(this);


    }

    private void initOkhttpHelp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient2);

    }


    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "im.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }


    private void setFragmentMode() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)

                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }


}
