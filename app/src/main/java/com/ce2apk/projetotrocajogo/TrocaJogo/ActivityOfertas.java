package com.ce2apk.projetotrocajogo.TrocaJogo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ce2apk.projetotrocajogo.R;

/**
 * Created by carlosbridi on 09/11/15.
 */
public class ActivityOfertas extends Fragment {

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View activitytrocas = inflater.inflate(R.layout.activityofertas, container, false);
        ((TextView)activitytrocas.findViewById(R.id.textView)).setText("Minhas ofertas");
        return activitytrocas;
    }

}
