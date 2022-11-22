package com.kookmin.kookbap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.kookmin.kookbap.Retrofits.RetrofitClient;
import com.kookmin.kookbap.cafeteriaFragments.CafeteriaViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendMenuFragment extends Fragment {
    Spinner timeSpinner;
    Spinner cafeteriaSpinner;
    String[] times = {"전체", "아침", "점심", "저녁"};
    String selectTime = times[0];
    String[] cafeterias = {"전체", "한울식당", "학생식당", "교직원식당", "K-BOB",
            "청향 한식당", "청향 양식당", "생활관식당 정기식"};
    JSONObject jsonObject;
    String date;

    ViewPager2 viewPager22;


    TabLayout recommendMenuTabLayout;
    RecyclerView recommendMenuBestRecyclerView;
    ArrayList<MenuData> recommendMenuData;
    MenuDataAdapter recommendMenuDataAdapter;
    CafeteriaViewPagerAdapter cafeteriaViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_menu, container, false);

        long now = System.currentTimeMillis();
        Date today = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(today);

        timeSpinner = view.findViewById(R.id.recommendMenuTimeSpinner);

        ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, times
        );
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        timeSpinner.setAdapter(timesAdapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectTime = times[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewPager22 = view.findViewById(R.id.viewPager22);

        Call<ArrayList<MenuData>> call; // 원래 Retrofit 은 받아올 데이터 클래스를 정의해야 하지만, 완전 통으로 가져올 때는 따로 정의 없이 Object로 가져올 수 있음
        call = RetrofitClient.getApiService().getRecommendMenuData("jihun");
        call.enqueue(new Callback<ArrayList<MenuData>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) { // 서버로부터 OK 사인을 받았을 때
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        // menu 띄워주는 adapter에 받아온 jsonObject을 넘김
                        cafeteriaViewPagerAdapter = new CafeteriaViewPagerAdapter(requireActivity(), jsonObject, date);
                        viewPager22.setAdapter(cafeteriaViewPagerAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });

        // Retrofit 으로 서버와 통신히여 menu 데이터를 받아오는 부분


//        recommendMenuBestRecyclerView = view.findViewById(R.id.recommendMenuBestRecyclerView);
//        recommendMenuData = new ArrayList<MenuData>();
//        recommendMenuDataAdapter = new MenuDataAdapter(recommendMenuData, getContext());
//
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        recommendMenuBestRecyclerView.setLayoutManager(linearLayoutManager);
//        recommendMenuBestRecyclerView.setAdapter(recommendMenuDataAdapter);
//
//        loadUserReviewsData();


        recommendMenuTabLayout = view.findViewById(R.id.recommendMenuTabLayout);
        recommendMenuTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                recommendMenuViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }
    public void loadUserReviewsData(){
//        recommendMenuData.add(new MenuData("menuName", "subMenuName", "price", "reviewText", 123, 4, 3));
        recommendMenuDataAdapter.notifyDataSetChanged();
    }

}