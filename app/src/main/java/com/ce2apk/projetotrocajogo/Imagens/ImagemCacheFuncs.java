package com.ce2apk.projetotrocajogo.Imagens;

import android.graphics.Bitmap;

/**
 * Created by carlosbridi on 03/02/16.
 */
public interface ImagemCacheFuncs {

    public String getCacheDir();
    public Bitmap loadImageCacheDir();
    public void loadImageRemoteServer();
    public void saveImageCacheDir(String imageString);
    public boolean imageInCacheDir();
}
