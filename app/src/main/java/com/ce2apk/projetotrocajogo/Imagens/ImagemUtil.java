package com.ce2apk.projetotrocajogo.Imagens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by carlosbridi on 01/02/16.
 */
public class ImagemUtil {

    public static Bitmap getBitmapFromString(String imgFotoDB){
        byte[] decodedString = Base64.decode(imgFotoDB, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;

    }

    public static String getStringFromBitmap(Bitmap imagem){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.PNG, 30, baos); // bm is the bitmap

        byte[] photo = baos.toByteArray();
        return Base64.encodeToString(photo, Base64.NO_WRAP);
    }
}
