package com.kookmin.kookbap;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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
    ArrayList<MenuData2> testRecommendMenuData;
    ArrayList<MenuData> menuData;
    MenuDataAdapter2 menuDataAdapter2;
    TextView testTextView;
    String userName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_menu, container, false);

        
        SharedPreferences userPrf = getActivity().getSharedPreferences("userData", MODE_PRIVATE);
        userName = userPrf.getString("ID", "");

        recommendMenuRecyclerView = view.findViewById(R.id.recommendMenuRecyclerView);
        testTextView = view.findViewById(R.id.testTextView);
        testTextView.setText("추천 메뉴 분석중...");

        Call<ArrayList<MenuData2>> call;
        call = RetrofitClient.getApiService().getRecommendMenuData(userName);
        call.enqueue(new Callback<ArrayList<MenuData2>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                    testRecommendMenuData = (ArrayList<MenuData2>) response.body();

                    testTextView.setText(userName+" 님, 이 메뉴는 어떠세요?");

                    menuDataAdapter2 = new MenuDataAdapter2(testRecommendMenuData, view.getContext());
                    recommendMenuRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    recommendMenuRecyclerView.setAdapter(menuDataAdapter2);

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