package livestreaming;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.quyuanjin.imseven.R;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import me.yokeyword.fragmentation.SupportActivity;

public class PushMain extends AppCompatActivity {
    JzvdStd myJzvdStd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_livestream);
        myJzvdStd = findViewById(R.id.jz_video1);
        myJzvdStd.setUp("rtmp://120.79.178.226:1935/live/1"
                , "直播拉流测试", JzvdStd.SCREEN_NORMAL, JZMediaIjk.class);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
