package com.quyuanjin.imseven.eventbus;

import android.graphics.Bitmap;

public class BitMapUrl {
   private Bitmap bitmap;

    public BitMapUrl(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
