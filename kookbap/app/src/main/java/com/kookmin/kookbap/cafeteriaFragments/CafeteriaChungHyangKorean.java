package com.kookmin.kookbap.cafeteriaFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kookmin.kookbap.R;
import com.kookmin.kookbap.MenuData;
import com.kookmin.kookbap.MenuDataAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CafeteriaChungHyangKorean extends Fragment {
    ArrayList<MenuData> reviewData;  // recyclerView 에 넘겨줄 ReviewData 객체를 가지고 있는 리스트
    MenuDataAdapter reviewDataAdapter;
    private final JSONObject jsonObject;
    String date;

    public CafeteriaChungHyangKorean(JSONObject jsonObject, String date) {
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
        ArrayList<String> boothNames = new ArrayList<>();

        try {
            JSONObject jsonObjectBoothNames = jsonObject.getJSONObject("청향 한식당(법학관 5층)").getJSONObject(date);
            Iterator<String> iter = jsonObjectBoothNames.keys();

            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            for (int i=0; i<boothNames.size(); i++) {
                String menu = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("메뉴");
                String price = jsonObjectBoothNames.getJSONObject(boothNames.get(i)).getString("가격");
                price = price.replaceAll("[^0-9]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
//                Log.e("menu", menu);
//                Log.e("price", price);

                String[] array = menu.split("\r\n");
                if (!((menu.equals("")) && (price.equals("")))) {
                    menu = array[0];
                    reviewData.add(new MenuData(menu, "아직 작성된 리뷰가 없습니다.", price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
//            Log.e("json2", jsonObject.toString());
        }
        recyclerView.setAdapter(reviewDataAdapter);

        return view;
    }
}