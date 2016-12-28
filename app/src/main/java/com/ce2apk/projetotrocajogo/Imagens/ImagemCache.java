package com.ce2apk.projetotrocajogo.Imagens;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;
import com.github.mrengineer13.snackbar.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by carlosbridi on 03/02/16.
 */
public class ImagemCache implements AsyncTaskCompleteListener, ImagemCacheFuncs, AsyncTaskCompleteImageCache{

    private int codJogo;
    private Bitmap imagemJogo;
    private Context context;


    private AsyncTaskCompleteImageCache callback;


    public int getCodJogo() {
        return codJogo;
    }

    public void setCodJogo(int codJogo) {
        this.codJogo = codJogo;
    }

    public Bitmap getImagemJogo() {
        return imagemJogo;
    }

    public void setImagemJogo(Bitmap imagemJogo) {
        this.imagemJogo = imagemJogo;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public AsyncTaskCompleteImageCache getCallback() {
        return callback;
    }

    public void setCallback(AsyncTaskCompleteImageCache callback) {
        this.callback = callback;
    }

    @Override
    public String getCacheDir() {
        return context.getCacheDir().getPath().toString();
    }

    @Override
    public Bitmap loadImageCacheDir() {

        try {
            File cacheFile = new File(getCacheDir(), String.valueOf(codJogo) + ".jpg");

            InputStream fileInputStream = new FileInputStream(cacheFile);

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

            bitmapOptions.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(fileInputStream, null, bitmapOptions);

        }catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void loadImageRemoteServer() {
        WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.GET_TASK, context, "Carregando imagem...", this);
        webServiceTask.execute(new String[]{consts.SERVICE_URL + "JogoImagem?idJogo="+ codJogo});
    }

    @Override
    public void saveImageCacheDir(String imageString) {
        String fileName = String.valueOf(codJogo) +".jpg";
        imagemJogo = ImagemUtil.getBitmapFromString(imageString);

        File filePicture = new File(getCacheDir(), fileName);

        FileOutputStream out = null;

        try{
            //out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            out = new FileOutputStream(filePicture);
            imagemJogo.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.write(fileName.getBytes());
            out.close();


            try {
                this.onTaskCompleteImageCache();
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean imageInCacheDir() {

        File file = new File(getCacheDir(), String.valueOf(codJogo) +".jpg");
        return file.exists();

    }

    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if (result.getString("imagemJogo")!=null){
            saveImageCacheDir(result.getString("imagemJogo"));
        }
    }

    @Override
    public void onTaskReturnNull(String msg) {
        //do anyting
    }

    @Override
    public void onTaskCompleteImageCache() throws JSONException {
        callback.onTaskCompleteImageCache();
    }
}
