package com.quyuanjin.imseven.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.quyuanjin.imseven.R;

public class KeyboardUtil {
    private View decorView;
    private View contentView;

    public KeyboardUtil(Fragment act, View contentView) {

        this.decorView = act.getActivity().getWindow().getDecorView();
        this.contentView = contentView;

        //only required on newer android versions. it was working on API level 19
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    public void enable() {
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    public void disable() {
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }


    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
       /*     Rect r = new Rect();
            decorView.getWindowVisibleDisplayFrame(r);
            int visibleHeight = r.height();
            int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
            int diff = height - r.bottom;

            if (diff != 0) {
                if (contentView.getPaddingBottom() != diff) {
                    contentView.setPadding(0, 0, 0, diff);
                }
            } else {
                if (contentView.getPaddingBottom() != 0) {
                    contentView.setPadding(0, 0, 0, 0);
                }
            }*/
            //获取当前根视图在屏幕上显示的大小
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            //计算出可见屏幕的高度
            int displayHight = rect.bottom - rect.top;
            //获得屏幕整体的高度
            int hight = decorView.getHeight();
            //获得键盘高度
            int keyboardHeight = hight - displayHight;
            LinearLayout linearLayout = contentView.findViewById(R.id.bottom_panel);

            boolean visible = keyboardHeight>100;
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();

            if (visible) {
                lp.bottomMargin=keyboardHeight;
                linearLayout.setLayoutParams(lp);
            }else {
                lp.bottomMargin=0;
                linearLayout.setLayoutParams(lp);
            }

        }
    };


    /**
     * Helper to hide the keyboard
     *
     * @param act
     */
    public static void hideKeyboard(Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
