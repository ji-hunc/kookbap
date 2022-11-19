package com.kookmin.kookbap.cafeteriaFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kookmin.kookbap.R;
import com.kookmin.kookbap.MenuData;
import com.kookmin.kookbap.MenuDataAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CafeteriaStudent extends Fragment {
    ArrayList<MenuData> reviewData;  // recyclerView 에 넘겨줄 ReviewData 객체를 가지고 있는 리스트
    MenuDataAdapter reviewDataAdapter;
    private final JSONObject jsonObject;
    String date;
    static ArrayList<String> menus = new ArrayList<>();


    public static ArrayList<String> getStudentMenus() {
        return menus;
    }

    public CafeteriaStudent(JSONObject jsonObject, String date) {
        this.jsonObject = jsonObject;
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria_hanul, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHanul);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reviewData = new ArrayList<>();
        reviewDataAdapter = new MenuDataAdapter(reviewData, requireActivity().getApplicationContext());

        MenuDataParser menuDataParser = new MenuDataParser(jsonObject, date);
        ArrayList<String> menuDatas = menuDataParser.getStudentMenuData();
        for (int i=0; i<menuDatas.size()/2; i++) {
            reviewData.add(new MenuData(menuDatas.get(i), "아직 작성된 리뷰가 없습니다.", menuDatas.get(menuDatas.size()/2 + i), "delicious", R.drawable.ic_setting, (float) (Math.random() * 5), 0, "학생식당"));
        }
        recyclerView.setAdapter(reviewDataAdapter);

        return view;
    }
}