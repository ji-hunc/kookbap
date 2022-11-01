package com.kookmin.kookbap.cafeteriaFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kookmin.kookbap.R;
import com.kookmin.kookbap.ReviewData;
import com.kookmin.kookbap.ReviewDataAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CafeteriaKBob extends Fragment {
    ArrayList<ReviewData> reviewData;  // recyclerView 에 넘겨줄 ReviewData 객체를 가지고 있는 리스트
    ReviewDataAdapter reviewDataAdapter;
    private JSONObject jsonObject;
    private RecyclerView recyclerView;

    public CafeteriaKBob(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafeteria_hanul, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHanul);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reviewData = new ArrayList<>();
        reviewDataAdapter = new ReviewDataAdapter(reviewData, getActivity().getApplicationContext());
        Log.e("json3", jsonObject.toString());

        ArrayList<String> boothNames = new ArrayList<>();
        try {
            JSONObject jsonObject2 = jsonObject.getJSONObject("K-Bob<sup>+</sup>").getJSONObject("2022-10-31");
            Log.e("json1", jsonObject2.toString());
            Iterator<String> iter = jsonObject2.keys();
            while (iter.hasNext()) {
                String boothName = iter.next().toString();
                boothNames.add(boothName);
            }

            for (int i=0; i<boothNames.size(); i++) {
                String menu = jsonObject2.getJSONObject(boothNames.get(i)).getString("메뉴");
                String price = jsonObject2.getJSONObject(boothNames.get(i)).getString("가격");
                Log.e("menu", menu);
                Log.e("price", price);

                String[] array = menu.split("\r\n");
                if (!((menu.equals("")) && (price.equals("")))) {
                    if (!((array[0].equals("운영시간")) || (array[0]).equals("＊ 회의 및 행사용 도시락의 경우 3일전 주문 필수"))) {
                        for (int j=0; j<array.length/3 + 1; j++) {
                            menu = array[3*j];
                            price = array[3*j+1];
                            reviewData.add(new ReviewData(menu, "아직 작성된 리뷰가 없습니다.", price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                        }
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json2", jsonObject.toString());
        }

        recyclerView.setAdapter(reviewDataAdapter);

        return view;
    }
}