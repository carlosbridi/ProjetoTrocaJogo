package com.ce2apk.projetotrocajogo.TrocaJogo.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ce2apk.projetotrocajogo.TrocaJogo.ActivityInteresses;
import com.ce2apk.projetotrocajogo.TrocaJogo.ActivityListaTrocas;
import com.ce2apk.projetotrocajogo.TrocaJogo.ActivityMeusJogos;


/**
 * Created by carlos.bridi on 18/11/2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    public boolean buscarDadosUsuario = false;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ActivityMeusJogos();
            case 1: return new ActivityListaTrocas();
            case 2: return new ActivityInteresses();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 3;
    }
}
