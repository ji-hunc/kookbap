package com.kookmin.kookbap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kookmin.kookbap.Retrofits.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendMenuFragment extends Fragment {
    JSONObject jsonObject;
    RecyclerView recommendMenuRecyclerView;
    MenuDataFromServer data;
    ArrayList<MenuDataFromServer> testRecommendMenuData;
    ArrayList<MenuData> menuData;
    MenuDataAdapter menuDataAdapter;
    TextView testTextView;
    String userName = "jihun"; // 유저 구현 시 변경 필요


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_menu, container, false);

        recommendMenuRecyclerView = view.findViewById(R.id.recommendMenuRecyclerView);
        testTextView = view.findViewById(R.id.testTextView);
        testTextView.setText("추천 메뉴 분석중...");

//        Call<ArrayList<MenuDataFromServer>> call;
//        call = RetrofitClient.getApiService().getRecommendMenuData(userName);
//        call.enqueue(new Callback<ArrayList<MenuDataFromServer>>() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) {
//                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
//                    testRecommendMenuData = (ArrayList<MenuDataFromServer>) response.body();
//                    menuData = new ArrayList<MenuData>();
//
//                    for (int i = 0; i < 5; i++){
//                        menuData.add(new MenuData(testRecommendMenuData.get(i).get_menu_name(), testRecommendMenuData.get(i).get_count_review(),
//                                testRecommendMenuData.get(i).get_price(), "delicious", R.drawable.ic_spoon, testRecommendMenuData.get(i).get_star_avg(),
//                                0, testRecommendMenuData.get(i).get_restaurant_name()));
//                    }
//
//                    testTextView.setText(userName+" 님, 이 메뉴는 어떠세요?");
//
//                    menuDataAdapter = new MenuDataAdapter(menuData, view.getContext());
//                    recommendMenuRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//                    recommendMenuRecyclerView.setAdapter(menuDataAdapter);
//
//                } else {
//                    testTextView.setText("추천 메뉴 분석 실패");
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
//                Log.e("Error", t.getMessage());
//            }
//        });




        return view;
    }

}