package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.Jogo.JogoInteresseCRUD;
import com.ce2apk.projetotrocajogo.Jogo.Temp_JogoCRUD;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.TrocaJogo.Adapters.JogoListAdapter;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.Util.ListaJogos;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;
import com.github.mrengineer13.snackbar.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;



/**
 * Created by carlosbridi on 17/12/15.
 */
public class ActivityListarJogos extends ListActivity implements AsyncTaskCompleteListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    private List<Jogo> mListaJogos;
    private ListaJogos lista = null;
    private int mAdicionarJogoColecao = 0; // 0 = Adicionando / 1 = Removendo
    private int mWebServiceRequest = 0; // 0 = Adicionando / 1 = Removendo
    private JogoListAdapter mAdapter;
    private ImageView imageViewJogoColecao;

    int[] iconIntArray = {R.drawable.ic_interesse, R.drawable.ic_interesse_marcado};
    int[] iconAddJogo = {R.drawable.ic_adicionar_black, R.drawable.ic_deletejogo};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultadopesquisajogos);

        registerForContextMenu(getListView());

        ActionBar bar =  getActionBar();
        if(bar != null)
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();


        Bundle extras = this.getIntent().getExtras();

        Temp_JogoCRUD temp_jogoCRUD = new Temp_JogoCRUD(getApplicationContext());

        lista = (ListaJogos) temp_jogoCRUD.listarJogo();
        mListaJogos = lista.getListaJogo();
        mAdapter = new JogoListAdapter(this, mListaJogos);
        setListAdapter(mAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getApplicationContext(), ActivityDetalheJogo.class);
                it.putExtra("jogo", (Serializable) mListaJogos.get(position));
                startActivity(it);
            }
        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_listajogos, menu);
    }


    public void adicionarInteresse(View view){
        mWebServiceRequest = 1;
        ImageView image = (ImageView) view.findViewById(R.id.imageInteresse);
        JogoInteresseCRUD jogoInteresseCRUD = new JogoInteresseCRUD(getBaseContext());

        final Jogo jogo = mListaJogos.get(getJogoList(view));

        if(!image.getTag().toString().equals("1")){
            if(jogoInteresseCRUD.inserirJogoInteresse(jogo) > 0) {
                WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, this, "Adicionando interesse...", this);
                webServiceTask.addParameter("idJogo", String.valueOf(jogo.getId()));
                webServiceTask.addParameter("idUsuario", String.valueOf(UsuarioUtil.obterUsuario(this, "dadosUsuario").getId()));
                webServiceTask.addParameter("idPlataforma", String.valueOf(jogo.getPlataforma().getId()));

                webServiceTask.execute(new String[]{consts.SERVICE_URL + "JogoInteresseWS"});

                doAnimationInteresses(image, 1);
            }
        }else{
            if(jogoInteresseCRUD.removerInteresse(jogo) > 0) {
                WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.DELETE_TASK, this, "Removendo interesse...", this);
                webServiceTask.execute(new String[]{consts.SERVICE_URL + "JogoInteresseWS?idJogo="+jogo.getId()+"&idUsuario="+ UsuarioUtil.obterUsuario(this, "dadosUsuario").getId()+"&idPlataforma="+jogo.getPlataforma().getId() });

                doAnimationInteresses(image, 0);
            }
        }

    }

    public int getJogoList(View view) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent().getParent().getParent().getParent();
        return listView.getPositionForView(parentRow);
    }


    public void adicionarJogo(View v){
        mWebServiceRequest = 0;
        imageViewJogoColecao = (ImageView) v.findViewById(R.id.imageJogoColecao);

        JogoCRUD jogoCRUD = new JogoCRUD(getBaseContext());

        final Jogo jogo = mListaJogos.get(getJogoList(v));

        if (imageViewJogoColecao.getTag().toString().equals("0")){
            if (jogoCRUD.inserirJogoColecao(jogo) > 0){
                WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, this, "Adicionando jogo", this);
                webServiceTask.addParameter("idJogo", String.valueOf(jogo.getId()));
                webServiceTask.addParameter("idUsuario", String.valueOf(UsuarioUtil.obterUsuario(this, "dadosUsuario").getId()));
                webServiceTask.addParameter("idPlataforma", String.valueOf(jogo.getPlataforma().getId()));

                webServiceTask.execute(new String[]{consts.SERVICE_URL + "JogoColecaoWS"});
                mAdicionarJogoColecao = 0;
            }else {
                Toast.makeText(getApplicationContext(), "Problemas ao inserir..", Toast.LENGTH_SHORT).show();
            }
        }else{
            if (jogoCRUD.removerJogoColecao(jogo) > 0){
                WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.DELETE_TASK, this, "Removendo jogo da coleção", this);
                webServiceTask.execute(new String[]{consts.SERVICE_URL + "JogoColecaoWS?idJogo="+jogo.getId()+"" +
                                                                                      "&idUsuario="+UsuarioUtil.obterUsuario(this, "dadosUsuario").getId()+
                                                                                      "&idPlataforma="+jogo.getPlataforma().getId()});
                mAdicionarJogoColecao = 1;
            }else{
                Toast.makeText(getApplicationContext(), "Problemas ao remover", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void doAnimationInteresses(final ImageView image, final int position){
        ScaleAnimation shrink =  new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animation animation) {

                // Change FAB color and icon
                image.setImageDrawable(getResources().getDrawable(iconIntArray[position], null));

                image.setTag(String.valueOf(position));

                // Scale up animation
                ScaleAnimation expand = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                image.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        image.startAnimation(shrink);
    }


    public void doAnimationJogo(final ImageView image, final int position){
        ScaleAnimation shrink =  new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animation animation) {

                // Change FAB color and icon
                image.setImageDrawable(getResources().getDrawable(iconAddJogo[position], null));

                image.setTag(String.valueOf(position));

                // Scale up animation
                ScaleAnimation expand = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                image.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        image.startAnimation(shrink);
    }


    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {
        if (!result.toString().equals("{}")){
            if (result.getString("codResultado").equals("1")) {
                Toast.makeText(this, result.getString("msgResultado"), Toast.LENGTH_SHORT).show();
                if (mWebServiceRequest == 0)
                    doAnimationJogo(imageViewJogoColecao, mAdicionarJogoColecao == 1 ? 0 : 1);
            }else{
                changeImageView(imageViewJogoColecao, mAdicionarJogoColecao);
            }

        }
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
    public boolean onClose() {
        getListView().setAdapter(mAdapter);;
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        displayResults(query + "*");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            displayResults(newText + "*");
        } else {
            getListView().setAdapter(mAdapter);
        }

        return false;
    }

    private void displayResults(String query){

    }


    public void changeImageView(ImageView imageView, int estadoImage){
        if (estadoImage == 0){
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_deletejogo, null));
            imageView.setTag("1");
            imageView.invalidate();
        }else{
            imageView.setImageResource(R.drawable.ic_adicionar_black);
            imageView.setTag("0");
        }

    }
}
