package com.kookmin.kookbap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kookmin.kookbap.Retrofits.RetrofitClient;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendMenuFragment extends Fragment {
    JSONObject jsonObject;
    RecyclerView recommendMenuRecyclerView;
    testRecommendMenuData data;
    ArrayList<testRecommendMenuData> testRecommendMenuData;
    ArrayList<MenuData> menuData;
    MenuDataAdapter menuDataAdapter;
    TextView testTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_menu, container, false);

        recommendMenuRecyclerView = view.findViewById(R.id.recommendMenuRecyclerView);
        testTextView = view.findViewById(R.id.testTextView);
        testTextView.setText("추천 메뉴 분석중...");

        Call<ArrayList<testRecommendMenuData>> call; // 원래 Retrofit 은 받아올 데이터 클래스를 정의해야 하지만, 완전 통으로 가져올 때는 따로 정의 없이 Object로 가져올 수 있음
        call = RetrofitClient.getApiService().getRecommendMenuData("jihun");
        call.enqueue(new Callback<ArrayList<testRecommendMenuData>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                    testRecommendMenuData = (ArrayList<testRecommendMenuData>) response.body();
                    menuData = new ArrayList<MenuData>();

                    for (int i = 0; i < 5; i++){
                        menuData.add(new MenuData(testRecommendMenuData.get(i).get_menu_name(), testRecommendMenuData.get(i).get_count_review(),
                                testRecommendMenuData.get(i).get_price(), "delicious", R.drawable.ic_spoon, testRecommendMenuData.get(i).get_star_avg(),
                                0, testRecommendMenuData.get(i).get_restaurant_name()));
                    }

                    testTextView.setText("오늘의 BEST 메뉴 : \n" + String.valueOf(menuData.get(0).getMenuName()));

                    menuDataAdapter = new MenuDataAdapter(menuData, view.getContext());
                    recommendMenuRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recommendMenuRecyclerView.setAdapter(menuDataAdapter);



                } else {
                    testTextView.setText("추천 메뉴 분석 실패");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });




        return view;
    }

}