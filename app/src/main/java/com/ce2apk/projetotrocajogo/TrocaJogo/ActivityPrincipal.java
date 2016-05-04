package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.ce2apk.projetotrocajogo.Jogo.JogoCRUD;
import com.ce2apk.projetotrocajogo.Jogo.JogoInteresseCRUD;
import com.ce2apk.projetotrocajogo.Jogo.Temp_JogoCRUD;
import com.ce2apk.projetotrocajogo.Jogo.Temp_JogoInteresseCRUD;
import com.ce2apk.projetotrocajogo.R;
import com.ce2apk.projetotrocajogo.Troca.TrocaCRUD;
import com.ce2apk.projetotrocajogo.Usuario.UsuarioUtil;
import com.ce2apk.projetotrocajogo.Util.SharedPref;
import com.ce2apk.projetotrocajogo.Util.SharedPrefDAO;
import com.ce2apk.projetotrocajogo.WebService.AsyncTaskCompleteListener;
import com.ce2apk.projetotrocajogo.WebService.WebServiceTask;
import com.ce2apk.projetotrocajogo.consts;
import com.github.mrengineer13.snackbar.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by carlosbridi on 03/11/15.
 */
public class ActivityPrincipal  extends AppCompatActivity
        implements AsyncTaskCompleteListener{

    int[] colorIntArray = {R.color.colorAccent};
    int[] iconIntArray = {R.drawable.ic_adicionar_white,R.drawable.ic_troca_white, R.drawable.ic_interesse_white,};
    FloatingActionButton fab;

    private int fCodUsuario = 0;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityprincipal);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            UsuarioUtil.gravarID(getApplicationContext(), extras.getInt("codResultado", 0));
            recuperarDadosUsuario(extras);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        registerClickFloatingButton(0);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Meus jogos"));
        tabLayout.addTab(tabLayout.newTab().setText("Trocas"));
        tabLayout.addTab(tabLayout.newTab().setText("Interesses"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.buscarDadosUsuario = getIntent().getExtras() != null;
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        registerClickFloatingButton(viewPager.getCurrentItem());

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                doAnimationFloatingButton(tab.getPosition());
                registerClickFloatingButton(tab.getPosition());

                HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv);
                View view = tabLayout.getRootView();
                int ScrolPos = view.getLeft() - (horizontalScrollView.getWidth() - tabLayout.getWidth()) / 2;
                horizontalScrollView.smoothScrollTo(ScrolPos, 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void recuperarDadosUsuario(Bundle extras){
        if (!UsuarioUtil.usuarioEmCache(this)) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("usuariologado", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("usuariologado", true);
            editor.commit();

            fCodUsuario = extras.getInt("codResultado", 0);

            WebServiceTask webServiceTask = new WebServiceTask(WebServiceTask.POST_TASK, this, "Buscando dados do usuário", this);
            webServiceTask.addParameter("id", String.valueOf(fCodUsuario));
            webServiceTask.execute(new String[]{consts.SERVICE_URL + "DadosUsuario"});
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == -1) && (requestCode == 1)){
            recuperarDadosUsuario(data.getExtras());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.perfil: {
                Intent itCadastro = new Intent(getApplicationContext(), ActivityCadastro.class);
                itCadastro.putExtra("atualizarDados", true);
                startActivityForResult(itCadastro, 1);

                return true;
            }
            case R.id.dadosEndereco:{

                Intent itDadosEndereco = new Intent(getApplicationContext(), ActivityCadastroEndereco.class);
                startActivityForResult(itDadosEndereco, 1);
                return true;
            }

            case R.id.logoff: {
                doLogoff();
                return true;
            }
            case R.id.sair:{
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void doLogoff(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("usuariologado", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("usuariologado", false);
        editor.commit();

        sharedPreferences = getApplicationContext().getSharedPreferences("jogosColecao", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("buscouDados", false);
        editor.commit();

        sharedPreferences = getApplicationContext().getSharedPreferences("interessesColecao", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("buscouDados", false);
        editor.commit();

        UsuarioUtil.removerDadosUsuario(this);

        JogoCRUD jogoCRUD = new JogoCRUD(this);
        jogoCRUD.removerJogos();

        JogoInteresseCRUD jogoInteresseCRUD = new JogoInteresseCRUD(this);
        Temp_JogoCRUD temp_jogoCRUD = new Temp_JogoCRUD(this);
        Temp_JogoInteresseCRUD temp_jogoInteresseCRUD = new Temp_JogoInteresseCRUD(this);

        jogoInteresseCRUD.removerJogosInteresse();
        temp_jogoCRUD.removerTodaColecao();
        temp_jogoInteresseCRUD.removerTodaColecao();

        TrocaCRUD trocaCRUD = new TrocaCRUD(this);
        trocaCRUD.deleteTrocas();

        Intent it = new Intent(this, ActivityLogin.class);
        startActivity(it);
        finish();
    }

    public void doAnimationFloatingButton(final int position){
        ScaleAnimation shrink =  new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                // Change FAB color and icon
                fab.setBackgroundTintList(getResources().getColorStateList(colorIntArray[0]));
                fab.setImageDrawable(getResources().getDrawable(iconIntArray[position], null));

                // Scale up animation
                ScaleAnimation expand = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(shrink);
    }

    public void teste(View view){
        Toast.makeText(this, "teste", Toast.LENGTH_SHORT).show();
    }

    public void registerClickFloatingButton(final int position){
        switch (position){
            case 0: {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(getApplicationContext(), ActivityIncluirJogoFiltro.class);
                        it.putExtra("favoritos", false);
                        startActivity(it);
                    }
                });
                return;
            }
            case 1:{
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(getApplicationContext(), ActivityNovaTroca.class);
                        startActivity(it);
                    }
                });
                return;
            }

            case 2:{
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(getApplicationContext(), ActivityIncluirJogoFiltro.class);
                        it.putExtra("interesses", true);
                        startActivity(it);
                    }
                });

                return;
            }

        }
    }


    @Override
    public void onTaskComplete(JSONObject result) throws JSONException {

        SharedPrefDAO dao = new SharedPrefDAO();

        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", String.valueOf(result.getInt("id")));

        dao.gravarSharedPref(new SharedPref("dadosUsuario", this, hashMap, MODE_PRIVATE));

        hashMap.put("nome", result.getString("nome"));
        hashMap.put("nomeusuario", result.getString("nomeUsuario"));
        hashMap.put("email", result.getString("email"));

        if (result.has("telefone"))
            hashMap.put("telefone", result.getString("telefone"));
        if (result.has("cep"))
            hashMap.put("cep", result.getString("cep"));
        if (result.has("logradouro"))
            hashMap.put("logradouro", result.getString("logradouro"));
        if (result.has("numero"))
            hashMap.put("numero", result.getString("numero"));
        if (result.has("complemento"))
            hashMap.put("complemento", result.getString("complemento"));
        if (result.has("bairro"))
            hashMap.put("bairro", result.getString("bairro"));
        if (result.has("estado"))
            hashMap.put("estado", result.getString("estado"));
        if (result.has("cidade"))
            hashMap.put("cidade", result.getString("cidade"));


        dao.gravarSharedPref(new SharedPref("dadosUsuario", this, hashMap, MODE_PRIVATE));
    }

    @Override
    public void onTaskReturnNull(String msg) {
        new SnackBar.Builder(this)
                .withMessage(msg)
                .withStyle(SnackBar.Style.DEFAULT)
                .withDuration((short)3000)
                .show();
    }

}
