package highperformancefriendscircle.ui.activitys;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.quyuanjin.imseven.R;

import highperformancefriendscircle.others.DataCenter;
import highperformancefriendscircle.widgets.EmojiPanelView;

public class EmojiPanelActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_panel);
        EmojiPanelView emojiPanelView = findViewById(R.id.emoji_panel_view);
        emojiPanelView.initEmojiPanel(DataCenter.emojiDataSources);
        findViewById(R.id.show).setOnClickListener(v -> emojiPanelView.showEmojiPanel());
    }
}
