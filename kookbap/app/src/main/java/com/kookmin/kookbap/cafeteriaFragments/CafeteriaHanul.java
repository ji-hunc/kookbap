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

public class CafeteriaHanul extends Fragment {
    ArrayList<ReviewData> reviewData;  // recyclerView 에 넘겨줄 ReviewData 객체를 가지고 있는 리스트
    ReviewDataAdapter reviewDataAdapter;
    private JSONObject jsonObject;
    private RecyclerView recyclerView;

    public CafeteriaHanul(JSONObject jsonObject) {
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
            JSONObject jsonObject2 = jsonObject.getJSONObject("한울식당(법학관 지하1층)").getJSONObject("2022-10-31");
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

                if (menu.equals("") || price.equals("")) {
                    // 특수한 경우: 메뉴와 가격 둘 중 하나가 비어있음
                    // ex
                    // "메뉴":"운영시간\r\n11시~18시30분","가격":""
                    // "메뉴":"","가격":""
                    // "메뉴":"[중식]\r\n불닭치즈라이스\r\n￦4500\r\n[석식]\r\n더진국\r\n얼큰국밥\r\n￦5500","가격":""
                    if (!(menu.equals("") && price.equals(""))) {
                        // 둘 다 비어있는 경우는 아예 취급 안함.
                        if (!(array[0].equals("운영시간"))) {
                            // 운영시간인 경우는 버림
                            // 결과적으로 남은 예외는
                            // "메뉴":"[중식]\r\n불닭치즈라이스\r\n￦4500\r\n[석식]\r\n더진국\r\n얼큰국밥\r\n￦5500","가격":""

                            // 하드코딩으로 처리
                            reviewData.add(new ReviewData(array[1], "아직 작성된 리뷰가 없습니다.", array[2], "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                            reviewData.add(new ReviewData(array[5], "아직 작성된 리뷰가 없습니다.", array[6], "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
                        }

                    }

                } else {
                    // 일반적인 경우: 메뉴와 가격 둘다 들어 있음
                    // ex
                    // "메뉴":"[중식]\r\n소고기칼국수\r\n&배추겉절이\r\n&공기밥","가격":"3700"
                    // "메뉴":"[중식]\r\n차돌된장비빔밥","가격":"4500"
                    if (array[0].equals("[중식]") || array[0].equals("[중석식]") || array[0].equals("[석식]")) { // 한울식당 메뉴이름 맨앞에 [중식] 필터링
                        menu = array[1];
                    } else {
                        menu = array[0];
                    }
                    reviewData.add(new ReviewData(menu, "아직 작성된 리뷰가 없습니다.", price, "delicious", R.drawable.ic_setting, (float) (Math.random()*5), 0));
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