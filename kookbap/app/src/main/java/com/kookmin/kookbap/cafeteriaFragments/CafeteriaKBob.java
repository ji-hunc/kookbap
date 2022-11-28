package com.kookmin.kookbap.cafeteriaFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kookmin.kookbap.MenuData2;
import com.kookmin.kookbap.MenuDataAdapter2;
import com.kookmin.kookbap.R;
import com.kookmin.kookbap.MenuData;
import com.kookmin.kookbap.MenuDataAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CafeteriaKBob extends Fragment {
    MenuDataAdapter2 menuDataAdapter2;
    private final ArrayList<MenuData2> todayMenus;
    String date;

    public CafeteriaKBob(ArrayList<MenuData2> todayMenus, String date) {
        this.todayMenus = todayMenus;
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria_hanul, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHanul);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        menuDataAdapter2 = new MenuDataAdapter2(todayMenus, requireActivity().getApplicationContext());
        recyclerView.setAdapter(menuDataAdapter2);

        return view;
    }
}