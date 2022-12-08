package com.kookmin.kookbap.cafeteriaFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kookmin.kookbap.MenuData;
import com.kookmin.kookbap.MenuDataAdapter;
import com.kookmin.kookbap.R;

import java.util.ArrayList;

public class CafeteriaDormitory extends Fragment {
    MenuDataAdapter menuDataAdapter;
    private final ArrayList<MenuData> todayMenus;
    String date;

    public CafeteriaDormitory(ArrayList<MenuData> todayMenus, String date) {
        this.todayMenus = todayMenus;
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria_menulists, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHanul);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        menuDataAdapter = new MenuDataAdapter(todayMenus, requireActivity().getApplicationContext());
        recyclerView.setAdapter(menuDataAdapter);

        return view;
    }
}