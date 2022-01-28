package livestreaming;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.quyuanjin.imseven.R;
import com.upyun.upplayer.widget.UpVideoView;


public class GetLiveStream extends Activity {

    String path = "rtmp://120.79.178.226:1935/live/1";
    RelativeLayout.LayoutParams mVideoParams;

    UpVideoView upVideoView;
    private EditText mPathEt;
    private TableLayout mHudView;
    private TextView mPlayInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ac_get_livestream);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mPathEt = (EditText) findViewById(R.id.editText);
        mHudView = (TableLayout) findViewById(R.id.hud_view);
        mPlayInfo = (TextView) findViewById(R.id.tv_info);
        mPlayInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHudView.getVisibility() == View.VISIBLE) {
                    mHudView.setVisibility(View.GONE);
                } else {
                    mHudView.setVisibility(View.VISIBLE);
                }
            }
        });

        upVideoView = (UpVideoView) findViewById(R.id.uvv_vido);
        upVideoView.setHudView(mHudView);

        //设置背景图片
//        upVideoView.setImage(R.drawable.dog);

        //设置播放地址
        upVideoView.setVideoPath(path);

        //开始播放
        upVideoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 重新开始播放器
        upVideoView.resume();
        upVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        upVideoView.pause();
    }

    public void toggle(View view) {

        if (upVideoView.isPlaying()) {

            //暂停播放
            upVideoView.pause();

        } else {

            //开始播放
            upVideoView.start();
        }
    }

    public void refresh(View view) {
        path = mPathEt.getText().toString();
        upVideoView.setVideoPath(path);
        upVideoView.start();
    }

    //全屏播放
    public void fullScreen(View view) {
        upVideoView.fullScreen(this);
    }

    private void fullScreen() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(metrics.widthPixels, metrics.heightPixels);
        mVideoParams = (RelativeLayout.LayoutParams) upVideoView.getLayoutParams();
        upVideoView.setLayoutParams(params);
        upVideoView.getTrackInfo();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (upVideoView.isFullState()) {
            //退出全屏
            upVideoView.exitFullScreen(this);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        upVideoView.release(true);
    }
}
