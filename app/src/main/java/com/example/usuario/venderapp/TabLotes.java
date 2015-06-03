package com.example.usuario.venderapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.example.usuario.venderapp.adapters.UrbanizacionAdapter;

import java.util.ArrayList;

/**
 * Created by USUARIO on 21-may-15.
 */
public class TabLotes extends Fragment {
    ArrayList<Urbanizacion> list;
    GridView grid;
    UrbanizacionAdapter adp;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_tab_lotes, container, false);
        return V;
    }


}
