package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Imagens.AsyncTaskCompleteImageCache;
import com.ce2apk.projetotrocajogo.Imagens.ImagemCache;
import com.ce2apk.projetotrocajogo.Imagens.ImagemUtil;
import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.Jogo.JogoInteresseCRUD;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Util.ParserArray;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.mrengineer13.snackbar.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityDetalheJogo extends AppCompatActivity implements AsyncTaskCompleteListener, AsyncTaskCompleteImageCache {

    private TextView _nomeJogo;
    private Jogo jogo;
    private TextView _categoriaJogo;
    private TextView _anoJogo;
    private TextView _descricaoJogo;

    private ViewGroup mRoot;
    private ImagemCache imagemCache = null;
    private SimpleDraweeView simpleDraweeView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
                /*Explode trans1 = new Explode();
                trans1.setDuration(3000);
                Fade trans2 = new Fade();
                trans2.setDuration(3000);

                getWindow().setEnterTransition( trans1 );
                getWindow().setReturnTransition( trans2 );*/

            TransitionInflater inflater = TransitionInflater.from( this );
            Transition transition = inflater.inflateTransition( R.transition.transitions );

            getWindow().setSharedElementEnterTransition(transition);

            Transition transition1 = getWindow().getSharedElementEnterTransition();
            transition1.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    TransitionManager.beginDelayedTransition(mRoot, new Slide());
                    _nomeJogo.setVisibility( View.VISIBLE );
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }

        Fresco.initialize(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        mRoot = (ViewGroup) findViewById(android.R.id.content);

        _nomeJogo = (TextView) findViewById(R.id.nomeJogo2);
        _categoriaJogo = (TextView) findViewById(R.id.detalhescategoriajogo);
        _anoJogo = (TextView) findViewById(R.id.detalhesanojogo);
        _descricaoJogo = (TextView) findViewById(R.id.detalhesdescricaojogo);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            jogo = (Jogo) extras.getSerializable("jogo");

            _nomeJogo.setText(jogo.getNomejogo());
            _categoriaJogo.setText("Categoria: "+ ParserArray.categoriaJogo(jogo.getCategoria()));
            _anoJogo.setText("Ano: " + String.valueOf(jogo.getAno()));
            _descricaoJogo.setText(jogo.getDescricao());
        }

        imagemCache = new ImagemCache();
        imagemCache.setCodJogo(jogo.getId());
        imagemCache.setContext(this);
        imagemCache.setCallback(this);

        simpleDraweeView = (SimpleDraweeView)findViewById(R.id.iv_car);

        if(imagemCache.imageInCacheDir()){
            simpleDraweeView.setImageBitmap(imagemCache.loadImageCacheDir());
        }else{
            simpleDraweeView.setImageBitmap(ImagemUtil.getBitmapFromString(jogo.getImagem())); //cache temporário até download
            imagemCache.loadImageRemoteServer();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car_activity, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView(item);
        }

        searchView.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );
        searchView.setQueryHint( getResources().getString(R.string.search_hint) );

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("jogo", jogo);
    }


    @Override
    public void onBackPressed() {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionManager.beginDelayedTransition(mRoot, new Slide());
            _nomeJogo.setVisibility(View.INVISIBLE);
        }

        super.onBackPressed();
    }


    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
    }

    @Override
    public void onTaskReturnNull(String msg) {
        new SnackBar.Builder(this)
                .withMessage(msg)
                .withStyle(SnackBar.Style.DEFAULT)
                .withDuration((short)3000)
                .show();
    }

    @Override
    public void onTaskCompleteImageCache() throws JSONException {
        simpleDraweeView.setImageBitmap(imagemCache.loadImageCacheDir());
    }
}
